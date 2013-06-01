package com.sdm.chat.daemon;

import com.sdm.util.Pair;

/**
 * @author sean@seandmurray.com (Sean D. Murray) thread safe class that will
 *         process user commands.
 */
public interface Command {

  public static enum Tag {
    INVALID, // An invalid command.
    LEAVE, // Leave the room.
    NICK, // Change your nick.
    SAY; // Say something to the room.
  }

  public static final String PREFIX = "/";

  /**
   * Pass in a string and determine if it is a command string. If it is not a
   * command string it is returned as a "SAY" command. If the String starts with
   * a command prefix then turn this into command tag and string pair. The
   * string will have the command and the command prefix removed. If there is an
   * error an invalid command tag is returned with a null string.
   */
  public Pair<Tag, String> parse(String message);
}