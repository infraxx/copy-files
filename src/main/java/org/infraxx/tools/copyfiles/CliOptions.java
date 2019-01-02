package org.infraxx.tools.copyfiles;

import org.apache.commons.cli.Option;

public class CliOptions {
  public static final Option INPUT_PATH = Option.builder("i")
      .desc("Input path.")
      .hasArg()
      .argName("Path to the root input directory")
      .build();

  public static final Option OUTPUT_PATH = Option.builder("o")
      .desc("Output path.")
      .hasArg()
      .argName("Path to the root output directory")
      .build();

  public static final Option DELETE_INPUT = Option.builder("d")
      .desc("Delete input after processing.")
      .hasArg(false)
      .build();

  public static final Option HELP = Option.builder("h")
      .longOpt("help")
      .desc("Show help message")
      .hasArg(false)
      .build();
}
