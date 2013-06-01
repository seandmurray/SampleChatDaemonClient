package com.sdm.chat.adaemon;

import com.sdm.chat.adaemon.ADaemon;
import com.sdm.util.Pair;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author sean@seandmurray.com (Sean D. Murray)
 * Tests for {@link ADaemon}.
 */
public class ADaemonTest {

  @Test
  public void testProcessCommandLineArguments() {
    
    // Test no arguments.
    String[] arguments = new String[0];
    Pair<Integer, Integer> pair = ADaemon.processCommandLineArguments(arguments);
    assertNull(pair);

    // Test one invalid non integer string.
    arguments = new String[1];
    arguments[0] = "invalidArg1";
    pair = ADaemon.processCommandLineArguments(arguments);
    assertNull(pair);

    // Test one string integer invalid argument.
    Integer arg1 = 0; // Invalid
    arguments[0] = arg1.toString();
    pair = ADaemon.processCommandLineArguments(arguments);
    assertNull(pair);

    // Test single valid integer string argument.
    arg1 = Integer.MAX_VALUE; // Valid
    arguments[0] = arg1.toString();
    pair = ADaemon.processCommandLineArguments(arguments);
    assertEquals(pair.getFirst(), arg1);
    // Check that the default max clients is set.
    assertEquals(pair.getSecond(), new Integer(ADaemon.DEFAULT_MAX_CLIENT_SIZE));

    // Test two valid integer string arguments.
    arguments = new String[2];
    arguments[0] = arg1.toString();
    arguments[1] = arg1.toString();
    pair = ADaemon.processCommandLineArguments(arguments);
    assertEquals(pair.getFirst(), arg1);
    assertEquals(pair.getSecond(), new Integer(Integer.MAX_VALUE));

    // Test invalid non integer string for second argument.
    arguments[0] = arg1.toString();
    arguments[1] = "invalidArg2";
    pair = ADaemon.processCommandLineArguments(arguments);
    assertNull(pair);

    // Test invalid integer string for second argument.
    Integer arg2 = 0; // Invalid
    arguments[1] = arg2.toString();
    pair = ADaemon.processCommandLineArguments(arguments);
    assertNull(pair);

    // Test three integer string arguments.
    arguments = new String[3];
    arguments[0] = arg1.toString();
    arguments[1] = arg1.toString();
    arguments[2] = arg1.toString();
    pair = ADaemon.processCommandLineArguments(arguments);
    assertNull(pair);
  }
}
