package com.sdm.chat.adaemon;

import com.sdm.chat.daemon.Command;
import com.sdm.util.Pair;

/**
 * @author sean@seandmurray.com (Sean D. Murray) thread safe class that will
 *         process user commands.
 */
public class ACommand implements Command {

  private static final String splitToken = "\\s+";

  /**
   * @see com.sdm.chat.daemon.Command#parseCommand(java.lang.String)
   **/
  @Override
  public Pair<Tag, String> parse(String message) {
    if (message.length() == 0) {
      return new Pair<Tag, String>().setFirst(Tag.INVALID);
    }
    if (message.startsWith(PREFIX)) {
      String[] tokens = message.split(splitToken);
      String command = tokens[0].trim().substring(1).toUpperCase();
      for (Tag tag : Tag.values()) {
        if (tag.toString().equals(command)) {
          return this.parseCommand(tag, message);
        }
      }
    } else {
      return new Pair<Tag, String>().setFirst(Tag.SAY).setSecond(message);
    }
    // We really shouldn't get here, but if we do, mark invalid.
    return new Pair<Tag, String>().setFirst(Tag.INVALID);
  }

  // Split the command and rest of a string into a pair.
  private Pair<Tag, String> parseCommand(Tag tag, String message) {
    Pair<Tag, String> commandPair = new Pair<Tag, String>();
    commandPair.setFirst(tag);
    int begin = tag.toString().length() + PREFIX.length();
    commandPair.setSecond(message.substring(begin).trim());
    return commandPair;
  }
}
