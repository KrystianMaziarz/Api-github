package com.pl.gtihub.api.Api.github.gtihub.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super(buildErrorMessage(username));
  }

  private static String buildErrorMessage(String username) {
    return String.format("User with username %s not found", username);
  }
}
