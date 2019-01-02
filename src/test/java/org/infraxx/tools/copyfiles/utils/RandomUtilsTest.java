package org.infraxx.tools.copyfiles.utils;

import java.util.Arrays;
import java.util.stream.Stream;
import org.infraxx.tools.copyfiles.Constants;
import org.junit.Test;

public class RandomUtilsTest {

  @Test
  public void testRandomString() {

    Constants.AB.codePoints().mapToObj(c->String.valueOf((char)c));
  }
}
