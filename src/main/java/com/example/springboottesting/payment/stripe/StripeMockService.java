package com.example.springboottesting.payment.stripe;

import com.example.springboottesting.payment.CardPaymentCharge;
import com.example.springboottesting.payment.CardPaymentCharger;
import com.example.springboottesting.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(value = "stripe.enabled", havingValue = "false")
public class StripeMockService implements CardPaymentCharger {
  @Override
  public CardPaymentCharge chargeCard(
      String cardSource, BigDecimal amount, Currency currency, String description) {
    return new CardPaymentCharge(true);
  }
}
