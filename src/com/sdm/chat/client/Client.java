/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.client;

import com.sdm.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author sean@seandmurray.com (Sean D. Murray)
 * The client side of the chat application.
 * Operates very much like telnet.
 * 
 * TODO(sean): Doesn't seem to disconnect well/correctly.
 */
public class Client {
    private String host;
    private int port;
    private Thread serverReader = null;
    private Socket socket = null;

    public static final String HELP = "Takes two required arguments: host, port.\n";
    
    public static void main(String[] arguments) {
      Pair<String, Integer> pair = processCommandLineArguments(arguments);
      if (pair == null) {
        // If the command line options did not parse then print help text.
        System.err.println(HELP);
      } else {
        new Client(pair.getFirst()/*host*/, pair.getSecond()/*port*/).run();
      }
    }
    
    /**
     * Two arguments, all required, in order:
     * host name or IP and port number.  Return
     * these arguments as a pair.
     */
    public static final Pair<String, Integer> processCommandLineArguments(String[] arguments) {
      Pair<String, Integer> pair = new Pair<String, Integer>();
      if (arguments.length < 2) {
        return null;
      }
      if (arguments[0].matches("^\\S+$")) {// One or more non white space character.
        pair.setFirst(arguments[0]);
      } else {
        return null;
      }
      if (arguments[1].matches("\\d+")) { // One or more numbers.
        pair.setSecond(Integer.parseInt(arguments[1]));
      } else {
        return null;
      }
      if (pair.getSecond() < 1) {
        return null;
      }
      return pair;
    }

    public Client(String host, int port) {
      this.host = host;
      this.port = port;
    }
    
    public void run() {
      try {
        socket = new Socket(host, port);

        // Read from the chat server and write to standard out. 
        // Use this thread. 
        serverReader = new Thread(new Pipe(socket.getInputStream(), System.out));
             
        // Read from standard in and write to chat server.
        // Put this in a new thread.
        Pipe serverWriter = new Pipe(System.in, socket.getOutputStream());
        
        serverReader.start();
        serverWriter.run();
      } catch(IOException e) {
        System.out.println(e.getMessage());
        return;
      }
    }

    private class Pipe implements Runnable {
      private BufferedReader reader;
      private PrintStream writer;
      
      Pipe(InputStream reader, OutputStream writer) {
        this.reader = new BufferedReader(new InputStreamReader(reader));
        this.writer = new PrintStream(writer);
      }
      
      public void run() {
        String message;
        try {
          while ((message = reader.readLine()) != null) {
            writer.print(message);
            writer.print("\r\n");
            writer.flush();
            if (socket.isClosed()) {
              System.exit(1);
            }
          }
        } catch(IOException e) {
          System.err.println(e.getMessage());
        }
      }
  }
}
