/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.chat.adaemon;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sdm.chat.adaemon.ACommand;
import com.sdm.chat.daemon.Command;
import com.sdm.chat.daemon.Command.Tag;
import com.sdm.util.Pair;

public class ACommandTest {

  private Command cmds;

  @Before
  public void setUp() throws Exception {
    this.cmds = new ACommand();
  }

  @Test
  public void testIvalidCmd() {
    String invalidCmd = "/some invalid command here";
    Pair<Tag, String> parsedCmd = this.cmds.parse(invalidCmd);
    Assert.assertEquals(Command.Tag.INVALID, parsedCmd.getFirst());
    Assert.assertNull(parsedCmd.getSecond());
  }

  @Test
  public void testLeaveCmd() {
    String prefix = "/leAvE \t ";
    String statement = "some stuff here!";
    this.testCmdOkWithStatement(prefix, statement, Command.Tag.LEAVE);
  }

  @Test
  public void testNickCmd() {
    String prefix = "/Nick     \t ";
    String statement = "some stuff here!";
    this.testCmdOkWithStatement(prefix, statement, Command.Tag.NICK);
  }

  @Test
  public void testSayWithoutPrefix() {
    String statement = "I say something WITH OUT a slash prefix!";
    this.testCmdOkWithStatement("", statement, Command.Tag.SAY);
  }

  @Test
  public void testSayWithPrefix() {
    String prefix = "/SaY\t ";
    String statement = "I say something WITH a slash prefix!";
    this.testCmdOkWithStatement(prefix, statement, Command.Tag.SAY);
  }

  private void testCmdOkWithStatement(String prefix, String statement,
      Tag expectedTag) {
    String cmd = prefix + statement;
    Pair<Tag, String> parsedCmd = this.cmds.parse(cmd);
    Assert.assertEquals(expectedTag, parsedCmd.getFirst());
    Assert.assertEquals(statement, parsedCmd.getSecond());
  }
}
