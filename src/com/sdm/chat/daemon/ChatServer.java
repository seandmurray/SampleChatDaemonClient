package com.sdm.chat.daemon;

import com.sdm.chat.adaemon.ADaemon;
import com.sdm.util.Pair;

/**
 * @author sean@seandmurray.com (Sean D. Murray) Start the chat server from
 *         here.
 */
public class ChatServer {

  public static void main(String[] commandLineArguments) {
    Pair<Integer, Integer> pair = ADaemon
        .processCommandLineArguments(commandLineArguments);
    if (pair == null) {
      // If the command line options did not parse then print help text.
      System.err.println(ADaemon.HELP);
    } else {
      new ADaemon(pair.getFirst()/* port */, pair.getSecond()/* max clients */)
          .run();
    }
  }
}
