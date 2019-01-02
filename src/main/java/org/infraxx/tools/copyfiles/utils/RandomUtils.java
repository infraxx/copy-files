package org.infraxx.tools.copyfiles.utils;

import java.util.Random;
import org.infraxx.tools.copyfiles.Constants;

public final class RandomUtils {

  private static Random rnd = new Random();

  private RandomUtils() {
  }

  public static String randomString(int len) {
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      sb.append(Constants.AB.charAt(rnd.nextInt(Constants.AB.length())));
    }

    return sb.toString();
  }
}
