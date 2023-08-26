package com.pl.gtihub.api.Api.github.gtihub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

  int status;
  String message;
}
