package com.example.springboottesting.utils;

import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.math.BigInteger;
import java.util.function.Predicate;

@Service
public class PhoneNumberValidator implements Predicate<String> {

  @Override
  public boolean test(String phoneNumber) {
    if (!phoneNumber.startsWith("+44")) return false;
    if (phoneNumber.length() != 12) return false;
    try {
      NumberUtils.parseNumber(phoneNumber.substring(1, 12), BigInteger.class);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }
}
