package com.sdm.chat.adaemon;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.SynchronousQueue;

import com.sdm.chat.daemon.ReturnCode;
import com.sdm.chat.daemon.Room;
import com.sdm.chat.daemon.User;

/**
 * @author sean@seandmurray.com (Sean D. Murray) Implementation of a room
 *         manager of for a chat room.
 */
public class ARoom implements Room {

  private static final String NICK_MESSAGE_SEPERATOR = " : ";

  // A blocking queue that will only ever had zero or one elements.
  private final SynchronousQueue<String> messageQueue = new SynchronousQueue<String>();

  // Collection of users in this room with nicks as their key.
  // Note, Hashtable is thread safe.
  private final Hashtable<String, User> users = new Hashtable<String, User>();

  public ARoom() {
    // Intentionally left blank.
  }

  @Override
  public ReturnCode join(User user) {
    if ((user == null) || (user.getNick() == null)) {
      return ReturnCode.NICK_INVALID;
    }
    if (this.users.contains(user.getNick())) {
      return ReturnCode.NICK_TAKEN;
    }
    this.users.put(user.getNick(), user);
    this.message(user.getNick(), ReturnCode.ROOM_JOINED.getDescription());
    return ReturnCode.OK;
  }

  @Override
  public boolean leave(String nick) {
    if ((nick != null) && (this.users.containsKey(nick))) {
      this.message(nick, ReturnCode.ROOM_LEFT.getDescription());
      this.users.remove(nick);
      return true;
    }
    return false;
  }

  @Override
  public void message(String nick, String message) {
    if (this.users.containsKey(nick)) {
      this.messageQueue.offer(nick + NICK_MESSAGE_SEPERATOR + message);
    }
  }

  @Override
  public void run() {
    while (true) {
      String message = "";
      try {
        message = this.messageQueue.take();
      } catch (InterruptedException e) {
        System.err.println(e.getMessage());
      }
      Enumeration<String> nicks = this.users.keys();
      while (nicks.hasMoreElements()) {
        this.users.get(nicks.nextElement()).messageToUser(message);
      }
    }
  }
}