/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.adaemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.sdm.chat.daemon.Command;
import com.sdm.chat.daemon.Daemon;
import com.sdm.chat.daemon.ReturnCode;
import com.sdm.chat.daemon.Room;
import com.sdm.chat.daemon.User;
import com.sdm.util.Pair;

/**
 * @author sean@seandmurray.com (Sean D. Murray)
 */
public class AUser implements User {

  /**
   * Read from the client and write to the room.
   */
  // Accessible for testing.
  class ClientToRoom implements Runnable {

    BufferedReader reader = null;
    User user = null;

    // For testing only.
    ClientToRoom() {
      // Intentionally left blank.
    }

    ClientToRoom(AUser user) throws IOException {
      this.reader = new BufferedReader(new InputStreamReader(
          AUser.this.clientSocket.getInputStream()));
      this.user = user;
    }

    @Override
    public void run() {
      try {
        String message = null;
        // Run until the reader fails.
        while ((message = this.reader.readLine()) != null) {
          this.user.messageToRoom(message);
        }
      } catch (IOException e) {
        this.user.logout(); // Clean up!
        System.err.println(e.getMessage());
      }
    }

    // For testing only.
    void setReader(BufferedReader reader) {
      this.reader = reader;
    }

    // For testing only.
    void setUser(AUser user) {
      this.user = user;
    }
  }

  private Socket clientSocket = null;
  private Thread clientToRoomThread = null;
  private final Command cmds = new ACommand();
  private Daemon daemon = null;
  // The nick is overloaded. Not only holds the nick but
  // if set this indicates the user is in a room
  // else if null then the user is not in a room
  private String nick = null;

  private Room room = null;

  private PrintStream writer = null;

  public AUser(Socket clientSocket, Room room, Daemon daemon) {
    this.daemon = daemon;
    this.room = room;
    this.clientSocket = clientSocket;
  }

  AUser() {
    // Intentionally left blank.
  }

  @Override
  public String getNick() {
    if (this.nick == null) {
      return null;
    }
    return new String(this.nick);
  }

  // For testing only
  @Override
  public void logout() {
    if (this.nick != null) {
      this.room.leave(this.nick);
    }
    try {// Do some clean up.
      if ((this.clientToRoomThread != null)
          && (!this.clientToRoomThread.isInterrupted())) {
        this.clientToRoomThread.interrupt();
      }
      if (!this.clientSocket.isClosed()) {
        this.clientSocket.close();
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    this.daemon.userLoggedOut();
  }

  @Override
  public void messageToRoom(String message) {
    Pair<Command.Tag, String> CommandParserPair = this.cmds.parse(message);
    if ((CommandParserPair.getFirst() == Command.Tag.SAY)
        && (this.nick != null)) {
      this.room.message(this.nick, CommandParserPair.getSecond());
    } else if (CommandParserPair.getFirst() == Command.Tag.NICK) {
      // Can't reset nick if it's already set.
      if (this.nick == null) {
        this.nick = CommandParserPair.getSecond();
        ReturnCode returnCode = this.room.join(this);
        if (ReturnCode.OK != returnCode) {
          this.messageToUser(ReturnCode.PERFIX_ERROR
              + returnCode.getDescription());
          this.nick = null; // Unset the nick as it's used to indicate if we
                            // joined a room.
        }
        // else ReturnCode.OK == returnCode, do nothing.
      }
    } else if (CommandParserPair.getFirst() == Command.Tag.LEAVE) {
      this.logout();
    } else {
      this.messageToUser(ReturnCode.PERFIX_ERROR
          + ReturnCode.COMMAND_INVALID.getDescription());
    }
  }

  @Override
  public void messageToUser(String message) {
    this.writer.print(message + "\n\r");
    this.writer.flush();
  }

  @Override
  public void run() {
    try {
      // The output stream to which we write back to the client.
      this.writer = new PrintStream(this.clientSocket.getOutputStream(), true);
      // Run the reading of data from the client side in a new thread.
      ClientToRoom clientToRoom = new ClientToRoom(this);
      this.clientToRoomThread = new Thread(clientToRoom);
      this.clientToRoomThread.start();
      // Welcome the user and send them a list of CommandParsers that may be
      // used.
      this.messageToUser(ADaemon.WELCOME);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      return;
    }
  }

  // Default scope for testing.
  void setClientSocket(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  // Default scope for testing.
  void setClientToRoomThread(Thread clientToRoomThread) {
    this.clientToRoomThread = clientToRoomThread;
  }

  // Default scope for testing.
  void setDaemon(Daemon daemon) {
    this.daemon = daemon;
  }

  // Default scope for testing.
  void setNick(String nick) {
    this.nick = nick;
  }

  // Default scope for testing.
  void setRoom(Room room) {
    this.room = room;
  }

  // Default scope for testing.
  void setWriter(PrintStream writer) {
    this.writer = writer;
  }
}
