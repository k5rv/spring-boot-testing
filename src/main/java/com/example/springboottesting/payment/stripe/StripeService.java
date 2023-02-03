package com.example.springboottesting.payment.stripe;

import com.example.springboottesting.payment.CardPaymentCharge;
import com.example.springboottesting.payment.CardPaymentCharger;
import com.example.springboottesting.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "stripe.enabled", havingValue = "true")
public class StripeService implements CardPaymentCharger {

  private static final RequestOptions requestOptions =
      RequestOptions.builder().setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc").build();
  private final StripeApi stripeApi;

  @Override
  public CardPaymentCharge chargeCard(
      String cardSource, BigDecimal amount, Currency currency, String description) {

    Map<String, Object> params = new HashMap<>();
    params.put("amount", amount);
    params.put("currency", currency);
    params.put("source", cardSource);
    params.put("description", description);

    try {
      Charge charge = stripeApi.create(params, requestOptions);
      Boolean chargePaid = charge.getPaid();
      return new CardPaymentCharge(chargePaid);
    } catch (StripeException e) {
      throw new IllegalStateException("Can not make stipe charge", e);
    }
  }
}
