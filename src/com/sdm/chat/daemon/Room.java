/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.daemon;

/**
 * @author sean@seandmurray.com (Sean D. Murray) Define the behaviors of a chat
 *         room.
 */
public interface Room extends Runnable {

  /**
   * A user joins the room with this nick. Return true if join works, else
   * false.
   */
  public ReturnCode join(User user);

  /**
   * A user with this nick leaves the room. Return true if leave works, else
   * false.
   */
  public boolean leave(String nick);

  /**
   * Receive a message from a user.
   */
  public void message(String nick, String message);
}
