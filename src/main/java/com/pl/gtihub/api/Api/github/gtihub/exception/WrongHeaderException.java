package com.pl.gtihub.api.Api.github.gtihub.exception;

public class WrongHeaderException extends RuntimeException {
  public WrongHeaderException(String header) {
    super(buildErrorMessage(header));
  }

  private static String buildErrorMessage(String header) {
    return String.format("Unsupported Accept header value: %s", header);
  }
}
