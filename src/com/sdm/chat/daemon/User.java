/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.daemon;

/**
 * @author sean@seandmurray.com (Sean D. Murray) Define the behaviors of a user
 *         on the server side.
 */
public interface User extends Runnable {

  /**
   * Get the nick of this user.
   */
  public String getNick();

  /**
   * log the user out.
   */
  public void logout();

  /**
   * Send a message string to the room.
   */
  public void messageToRoom(String message);

  /**
   * Write a message to this user.
   */
  public void messageToUser(String message);
}
