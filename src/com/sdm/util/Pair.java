/***********************************************************
[2010] - [2013] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.
***********************************************************/
package com.sdm.util;

/**
 * @author sean@seandmurray.com (Sean D. Murray) A simple class to pair two
 *         generics together.
 */
public class Pair<A, B> {
  private A a = null;
  private B b = null;

  public A getFirst() {
    return this.a;
  }

  public B getSecond() {
    return this.b;
  }

  public Pair<A, B> setFirst(A a) {
    this.a = a;
    return this;
  }

  public Pair<A, B> setSecond(B b) {
    this.b = b;
    return this;
  }
}
