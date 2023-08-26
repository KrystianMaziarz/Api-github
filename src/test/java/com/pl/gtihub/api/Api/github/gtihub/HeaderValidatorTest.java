package com.pl.gtihub.api.Api.github.gtihub;

import static com.pl.gtihub.api.Api.github.gtihub.GithubDataFactory.VALID_HEADER;
import static org.junit.jupiter.api.Assertions.*;

import com.pl.gtihub.api.Api.github.gtihub.exception.WrongHeaderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class HeaderValidatorTest {

  private final HeaderValidator headerValidator = new HeaderValidator();

  @ParameterizedTest
  @NullSource
  @ValueSource(
      strings = {
        "application/xml",
        "application/zxzcz",
        "application/jzonn",
        "application",
        " ",
        "application/   json",
        "json"
      })
  void shouldThrowWrongHeaderExceptionWhenHeaderIsNotCorrect(String acceptHeader) {
    // given //when
    var thrown =
        assertThrows(WrongHeaderException.class, () -> headerValidator.checkHeader(acceptHeader));

    // then
    assertTrue(thrown.getMessage().contains("Unsupported Accept header value: " + acceptHeader));
  }

  @Test
  void shouldNotThrowAnyExceptionWhenHeaderIsCorrect() {
    // given //when //then
    assertDoesNotThrow(() -> headerValidator.checkHeader(VALID_HEADER));
  }
}
