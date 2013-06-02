/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.daemon;

/**
 * @author sean@seandmurray.com (Sean D. Murray) The behavior of a daemon chat
 *         server.
 */
public interface Daemon {

  /**
   * Start your engines!
   */
  public void run();

  /**
   * A user logged out and thus there is room for a new client.
   */
  public void userLoggedOut();
}
