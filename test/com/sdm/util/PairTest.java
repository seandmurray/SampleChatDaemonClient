/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PairTest {

	  private Pair<Integer, Integer> pair = null;

	  @Before
	  public void setUp() {
	    pair = new Pair<Integer, Integer>();
	  }

	  /**
	   * Test method for {@link Pair#setFirst(Object)}.
	   * and {@link Pair#getFirst()}.
	   */
	  @Test
	  public void testGetSetFirst() {
	    Integer first = Integer.MAX_VALUE;
	    pair.setFirst(first);
	    assertEquals(first, pair.getFirst());
	  }

	  /**
	   * Test method for {@link Pair#setSecond(Object)}.
	   * and {@link Pair#getSecond()}.
	   */
	  @Test
	  public void testGetSetLast() {
	    Integer last = Integer.MIN_VALUE;
	    pair.setSecond(last);
	    assertEquals(last, pair.getSecond());
	  }
}
