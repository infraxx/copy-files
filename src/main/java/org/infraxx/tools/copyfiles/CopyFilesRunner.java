package org.infraxx.tools.copyfiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.infraxx.tools.copyfiles.task.CopyTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CopyFilesRunner implements CommandLineRunner {
  private static final Logger LOG = LoggerFactory.getLogger(CopyFilesRunner.class);

  private final CopyTask copyTask;

  public CopyFilesRunner(CopyTask copyTask) {
    this.copyTask = copyTask;
  }

  @Override
  public void run(String... args) {
    LOG.info("Starting ...");
    CommandLine cmd = parseArgs(args);

    if (cmd.hasOption(CliOptions.INPUT_PATH.getOpt()) && cmd.hasOption(CliOptions.OUTPUT_PATH.getOpt())) {
      copy(Paths.get(cmd.getOptionValue(CliOptions.INPUT_PATH.getOpt())),
          Paths.get(cmd.getOptionValue(CliOptions.OUTPUT_PATH.getOpt())),
          cmd.hasOption(CliOptions.DELETE_INPUT.getOpt()));
    }
  }

  private void copy(Path in, Path out, boolean deleteInput) {
    copyTask.run(in, out, deleteInput);
  }

  private static Path getPath(Option option) {
    String suppliedPath = option.getValue();
    return Paths.get(suppliedPath).normalize().toAbsolutePath();
  }

  private static CommandLine parseArgs(String[] args) {
    Options options = buildCliOptions();
    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(options, args);

      if (cmd.getOptions().length == 0 || cmd.hasOption(CliOptions.HELP.getOpt())) {
        printCliHelp(options);
      }
      return cmd;
    } catch (ParseException e) {
      LOG.error(e.toString());
      throw new RuntimeException(e);
    }
  }

  private static void printCliHelp(Options options) {
    new HelpFormatter().printHelp("copy-files", options);
  }


  private static Options buildCliOptions() {
    Options options = new Options();
    options.addOption(CliOptions.INPUT_PATH);
    options.addOption(CliOptions.OUTPUT_PATH);
    options.addOption(CliOptions.DELETE_INPUT);
    options.addOption(CliOptions.HELP);
    return options;
  }
}
