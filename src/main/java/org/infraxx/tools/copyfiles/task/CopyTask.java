package org.infraxx.tools.copyfiles.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.infraxx.tools.copyfiles.Constants;
import org.infraxx.tools.copyfiles.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CopyTask {

  private static final Logger LOG = LoggerFactory.getLogger(CopyTask.class);

  @Value("${cf.base-name-length:64}")
  private int baseNameLength;

  public void run(Path inputPath, Path outputPath, boolean deleteInput) {
    // check for input existence -> exit
    LOG.info("Input directory: {}", inputPath);
    if (Files.notExists(inputPath)) {
      String errorMessage = "Input location doesn't exist: " + inputPath;
      LOG.error(errorMessage);
      throw new CopyFilesException(errorMessage);
    }

    // check or create dest tree
    createDestinationTree(outputPath);

    // traverse input tree with file type filter
    traverseInputTree(inputPath,
        outputPath,
        (from, to) -> {
          try {
            if (deleteInput) {
              Files.move(from, to);
            } else {
              Files.copy(from, to);
            }
          } catch (IOException e) {
            LOG.warn("Can't copy {} to {}", from, to);
            LOG.error(e.toString(), e);
          }
        });
  }

  private void createDestinationTree(Path outputPath) {
    LOG.info("Creating destination directory tree under {}", outputPath);
    try {
      createDirectory(outputPath);
    } catch (IOException e) {
      String errorMessage = "Can't create root output directory: " + outputPath;
      LOG.error(errorMessage, e);
      throw new CopyFilesException(errorMessage, e);
    }

    Constants.AB.codePoints()
        .mapToObj(c -> String.valueOf((char) c))
        .forEach(c -> {
          Path dir = null;
          try {
            dir = outputPath.resolve(c);
            LOG.info("Creating output directory {}", dir);
            if (Files.notExists(dir)) {
              Files.createDirectory(dir);
            }
          } catch (IOException e) {
            String errorMessage = "Can't create output directory: " + dir;
            LOG.error(errorMessage, e);
            throw new CopyFilesException(errorMessage, e);
          }
        });
  }

  private void createDirectory(Path outputPath) throws IOException {
    if (Files.notExists(outputPath)) {
      Files.createDirectory(outputPath);
    }
  }

  private void traverseInputTree(Path inputDir, Path outputDir, BiConsumer<Path, Path> copyFun) {
    LOG.info("Input: {}", inputDir);
    try (Stream<Path> entries = Files.walk(inputDir)) {
      entries
          .filter(e -> !e.equals(inputDir))
          .forEach(e -> {
            if (!Files.isDirectory(e)) {
              String inputFileName = e.getFileName().toString();
              String extension = FilenameUtils.getExtension(inputFileName);
              String newBaseName = RandomUtils.randomString(baseNameLength);
              String newFileName = newBaseName + "." + extension;
              Path dest = outputDir
                  .resolve(newFileName.substring(0, 1))
                  .resolve(newFileName);
              LOG.info("{} -> {}", e, dest);
              copyFun.accept(e, dest);
            }
          });
    } catch (IOException e) {
      String errorMessage = "Can't traverse directory: " + inputDir;
      LOG.error(errorMessage, e);
      throw new CopyFilesException(errorMessage, e);
    }
  }
}
