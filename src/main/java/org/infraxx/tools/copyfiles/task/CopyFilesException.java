package org.infraxx.tools.copyfiles.task;

public class CopyFilesException extends RuntimeException {

  public CopyFilesException(String message) {
    super(message);
  }

  public CopyFilesException(String message, Throwable cause) {
    super(message, cause);
  }
}
