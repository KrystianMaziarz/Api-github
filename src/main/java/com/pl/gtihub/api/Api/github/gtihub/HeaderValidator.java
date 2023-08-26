package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.exception.WrongHeaderException;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class HeaderValidator {
  private static final String JSON_HEADER = "application/json";

  public void checkHeader(String header) {
    Optional.ofNullable(header)
        .filter(acceptHeader -> JSON_HEADER.equals(header))
        .orElseThrow(() -> new WrongHeaderException(header));
  }
}
