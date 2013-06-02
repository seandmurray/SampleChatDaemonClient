/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.adaemon;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.Test;

import com.sdm.chat.adaemon.AUser;
import com.sdm.chat.adaemon.AUser.ClientToRoom;
import com.sdm.chat.daemon.Command;
import com.sdm.chat.daemon.Daemon;
import com.sdm.chat.daemon.ReturnCode;
import com.sdm.chat.daemon.Room;

/**
 * @author sean@seandmurray.com (Sean D. Murray)
 */
public class AUserTest {

  @Test
  public void testClientToRoom_run_exception() throws Exception {
    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser userMock = control.createMock(AUser.class);
    BufferedReader readerMock = control.createMock(BufferedReader.class);
    ClientToRoom clientToRoom = new AUser().new ClientToRoom();
    clientToRoom.setReader(readerMock);
    clientToRoom.setUser(userMock);

    // Set up the expected behavior.
    expect(readerMock.readLine()).andThrow(new IOException());
    userMock.logout();
    expectLastCall().once();

    // Run the test
    control.replay();
    clientToRoom.run();
    control.verify();
  }

  @Test
  public void testClientToRoom_run_OK() throws Exception {
    String message = "sometestmessage";

    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser userMock = control.createMock(AUser.class);
    BufferedReader readerMock = control.createMock(BufferedReader.class);
    ClientToRoom clientToRoom = new AUser().new ClientToRoom();
    clientToRoom.setReader(readerMock);
    clientToRoom.setUser(userMock);

    // Set up the expected behavior.
    expect(readerMock.readLine()).andReturn(message);
    userMock.messageToRoom(message);
    expectLastCall().once();
    expect(readerMock.readLine()).andReturn(null);

    // Run the test.
    control.replay();
    clientToRoom.run();
    control.verify();
  }

  @Test
  public void testMessage_COMMAND_INVALID() throws Exception {
    String nick = "someNick";
    String message = Command.PREFIX + "someinvalidcommand";

    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser user = new AUser();
    Room roomMock = control.createMock(Room.class);
    PrintStream writerMock = control.createMock(PrintStream.class);
    user.setNick(nick);
    user.setRoom(roomMock);
    user.setWriter(writerMock);

    writerMock.print(ReturnCode.PERFIX_ERROR
        + ReturnCode.COMMAND_INVALID.getDescription() + "\n\r");
    expectLastCall().once();
    writerMock.flush();
    expectLastCall().once();

    // Run the test.
    control.replay();
    user.messageToRoom(message);
    control.verify();
  }

  @Test
  public void testMessage_LEAVE() throws Exception {
    String nick = "someNick";
    String message = Command.PREFIX + Command.Tag.LEAVE;

    // Set up the frame work for testing.
    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser user = new AUser();
    Room roomMock = control.createMock(Room.class);
    Thread clientRoomThreadMock = control.createMock(Thread.class);
    Socket clientSocketMock = control.createMock(Socket.class);
    Daemon daemonMock = control.createMock(Daemon.class);
    user.setNick(nick);
    user.setRoom(roomMock);
    user.setClientToRoomThread(clientRoomThreadMock);
    user.setClientSocket(clientSocketMock);
    user.setDaemon(daemonMock);

    expect(roomMock.leave(nick)).andReturn(true);
    expect(clientRoomThreadMock.isInterrupted()).andReturn(false);
    clientRoomThreadMock.interrupt();
    expectLastCall().once();
    expect(clientSocketMock.isClosed()).andReturn(false);
    clientSocketMock.close();
    expectLastCall().once();
    daemonMock.userLoggedOut();
    expectLastCall().once();

    // Run the test.
    control.replay();
    user.messageToRoom(message);
    control.verify();
  }

  @Test
  public void testMessage_NICK_INVALID() throws Exception {
    String nick = "someNick";
    String message = Command.PREFIX + Command.Tag.NICK + " " + nick;

    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser user = new AUser();
    Room roomMock = control.createMock(Room.class);
    PrintStream writerMock = control.createMock(PrintStream.class);
    user.setRoom(roomMock);
    user.setWriter(writerMock);

    expect(roomMock.join(user)).andReturn(ReturnCode.NICK_INVALID);
    writerMock.print(ReturnCode.PERFIX_ERROR
        + ReturnCode.NICK_INVALID.getDescription() + "\n\r");
    expectLastCall().once();
    writerMock.flush();
    expectLastCall().once();

    // Run the test.
    control.replay();
    user.messageToRoom(message);
    control.verify();
    assertNull(user.getNick());
  }

  @Test
  public void testMessage_NICK_OK() throws Exception {
    String nick = "someNick";
    String message = Command.PREFIX + Command.Tag.NICK + " " + nick;

    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser user = new AUser();
    Room roomMock = control.createMock(Room.class);
    user.setRoom(roomMock);

    expect(roomMock.join(user)).andReturn(ReturnCode.OK);

    // Run the test.
    control.replay();
    user.messageToRoom(message);
    control.verify();
    assertEquals(user.getNick(), nick);
  }

  @Test
  public void testMessage_SAY() throws Exception {
    String nick = "someNick";
    String message = "Some message to the channel.";

    // Set up the frame work for testing.
    IMocksControl control = EasyMock.createControl();
    AUser user = new AUser();
    Room roomMock = control.createMock(Room.class);
    user.setNick(nick);
    user.setRoom(roomMock);

    roomMock.message(nick, message);
    expectLastCall().once();

    // Run the test.
    control.replay();
    user.messageToRoom(message);
    control.verify();
  }
}
