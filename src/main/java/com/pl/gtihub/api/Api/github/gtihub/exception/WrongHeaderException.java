package com.pl.gtihub.api.Api.github.gtihub.exception;

public class WrongHeaderException extends RuntimeException {
  public WrongHeaderException(String header) {
    super("Unsupported Accept header value: " + header);
  }
}
