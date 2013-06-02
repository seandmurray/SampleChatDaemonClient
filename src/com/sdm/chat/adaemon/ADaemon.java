/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.adaemon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sdm.chat.daemon.Daemon;
import com.sdm.chat.daemon.Room;
import com.sdm.chat.daemon.User;
import com.sdm.util.Pair;

/**
 * @author sean@seandmurray.com (Sean D. Murray) The daemon process that listens
 *         on a default of input port.
 * 
 *         Starts a set number of handlers waiting for client connections. The
 *         1st handler will wait for a connection and all the others will be
 *         held in suspension waiting on a synchronization block. This makes the
 *         connection time very fast but we pay for it in server start up time.
 */
public class ADaemon implements Daemon {

  // Exists to make testing easier.
  // Default access for testing purposes.
  public class UserFactory {

    UserFactory() {
      // Intentionally left blank.
    }

    User newUser(Socket socket, Room room, Daemon daemon) {
      return new AUser(socket, room, daemon);
    }
  }

  // The default initial and max number of clients that may connect.
  public static final int DEFAULT_MAX_CLIENT_SIZE = 10;

  // The help string that is printed when start arguments fails.
  public static final String HELP = "Takes one or two arguments.\n"
      + "The first argument is the server port number, this is required.\n"
      + "The second optional argument is the max number of clients allowed\n";

  // The welcome string the is printed when a client first connects.
  public static final String WELCOME = "COMMANDS:\n" + "/NICK <somenickhere>\n"
      + "  You are required to set a nick before joining the room.\n"
      + "  Once you nick is set, it can not be reset.\n\n"
      + "/SAY <say something to the room>\n" + "  Talk to the room.\n"
      + "  You may omit the /SAY part and it is infered.\n\n" + "/LEAVE\n"
      + "  Leave the chat server.\n";

  /**
   * Take a string array and convert that to a pair of integer arguments. The
   * first required argument is expected to be the server port and the second is
   * optional and will be the max allowed clients. A default value is used for
   * the max clients if one is not given.
   * 
   * @param arguments
   *          is a list of strings that is expected to contain one or two
   *          values.
   * 
   * @return a pair of arguments, the first is the server port number, the
   *         second is the max number clients that may connect simultaneously.
   *         If the return is null then arguments failed.
   */
  public static final Pair<Integer, Integer> processCommandLineArguments(
      String[] arguments) {
    Pair<Integer, Integer> pair = new Pair<Integer, Integer>()
        .setSecond(DEFAULT_MAX_CLIENT_SIZE);
    if ((arguments.length < 1) || (arguments.length > 2)) {
      return null;
    }
    if (arguments[0].matches("\\d+")) { // One or more numbers.
      pair.setFirst(Integer.parseInt(arguments[0]));
    } else {
      return null;
    }
    if (arguments.length > 1) {
      if (arguments[1].matches("\\d+")) {
        pair.setSecond(Integer.parseInt(arguments[1]));
      } else {
        return null;
      }
    }
    if ((pair.getFirst() < 1) || (pair.getSecond() < 1)) {
      return null;
    }
    return pair;
  }

  private int maxClients = DEFAULT_MAX_CLIENT_SIZE;
  private Room room = null;
  private ServerSocket serverSocket = null;

  private UserFactory userFactory = null;

  public ADaemon(int port) {
    this(port, DEFAULT_MAX_CLIENT_SIZE);
  }

  public ADaemon(int port, int maxClients) {
    try {
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException("Could not initalize server " + e);
    }
    this.maxClients = maxClients;
    this.room = new ARoom();
    this.userFactory = new UserFactory();
  }

  // Constructor for testing only.
  ADaemon() {
    // Intentionally left blank.
  }

  @Override
  public void run() {
    // Spin The room off into it's own thread.
    new Thread(this.room).start();

    // Create a set of waiting threads for each of the allowed number of
    // clients.
    for (int i = 0; i < this.maxClients; i++) {
      this.newUser();
    }
  }

  @Override
  public void userLoggedOut() {
    this.newUser();
  }

  /**
   * This is the critical section. Each handler will queue up waiting for a
   * connection to be made; a connection will free up the synchronization lock
   * and the next handler will step up and wait for another connection.
   **/
  synchronized void newUser() {
    try {
      User user = this.userFactory.newUser(this.serverSocket.accept(),
          this.room, this);
      new Thread(user).start();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  // For testing only
  void setMaxClients(int maxClients) {
    this.maxClients = maxClients;
  }

  // For testing only
  void setRoom(Room room) {
    this.room = room;
  }

  // For testing only
  void setServerSocket(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  // For testing only
  void setUserFactory(UserFactory userFactory) {
    this.userFactory = userFactory;
  }
}
