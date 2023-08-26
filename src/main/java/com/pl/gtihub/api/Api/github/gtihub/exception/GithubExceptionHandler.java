package com.pl.gtihub.api.Api.github.gtihub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GithubExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleNotFoundException(UserNotFoundException ex) {
    return new ResponseEntity<>(
        new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(WrongHeaderException.class)
  public ResponseEntity<ErrorMessage> handleWrongHeaderException(WrongHeaderException ex) {
    return new ResponseEntity<>(
        new ErrorMessage(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
        HttpStatus.NOT_ACCEPTABLE);
  }
}
