package com.sdm.chat.daemon;

/**
 * @author sean@seandmurray.com (Sean D. Murray) A place to keep all the return
 *         codes and their descriptions.
 */
public enum ReturnCode {
  COMMAND_INVALID("Invalid command!"), NICK_INVALID("This nick is invalid!"), NICK_TAKEN(
      "This nick is already in use!"), NICK_SET("Nick has been set!"), ROOM_JOINED(
      "Has joined the room!"), ROOM_LEFT("Has left the room!"), OK(null);

  public static final String PERFIX_ERROR = "ERROR: ";
  public static final String PERFIX_NOTIFICATION = "SYSTEM: ";

  private String description = null;

  ReturnCode(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}