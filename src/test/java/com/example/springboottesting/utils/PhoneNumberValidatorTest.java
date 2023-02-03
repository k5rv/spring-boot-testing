package com.example.springboottesting.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PhoneNumberValidatorTest {

  private PhoneNumberValidator underTest;

  @BeforeEach
  void setUp() {
    underTest = new PhoneNumberValidator();
  }

  @ParameterizedTest
  @CsvSource({
    "+44700000000, TRUE",
    "+447000000000, FALSE",
    "+447000000000, FALSE",
    "+44a00000000, FALSE"
  })
  void itShouldValidatePhoneNumber(String phoneNumber, boolean expected) {
    boolean isValid = underTest.test(phoneNumber);
    Assertions.assertThat(isValid).isEqualTo(expected);
  }
}
