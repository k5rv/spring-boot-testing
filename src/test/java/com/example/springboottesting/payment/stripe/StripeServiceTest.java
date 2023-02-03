package com.example.springboottesting.payment.stripe;

import com.example.springboottesting.payment.CardPaymentCharge;
import com.example.springboottesting.payment.Currency;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class StripeServiceTest {

  private StripeService underTest;

  @Mock private StripeApi stripeApi;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    underTest = new StripeService(stripeApi);
  }

  @Test
  void itShouldChargeCard() throws Exception {
    // Given
    String cardSource = "0x0x0x";
    BigDecimal amount = new BigDecimal("10.00");
    Currency currency = Currency.USD;
    String description = "Cat purchase";
    Charge charge = new Charge();
    charge.setPaid(true);
    given(stripeApi.create(anyMap(), any())).willReturn(charge);
    // When
    CardPaymentCharge cardPaymentCharge =
        underTest.chargeCard(cardSource, amount, currency, description);

    // Then

    ArgumentCaptor<Map<String, Object>> requestMapArgumentCaptor =
        ArgumentCaptor.forClass(Map.class);

    ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor =
        ArgumentCaptor.forClass(RequestOptions.class);

    then(stripeApi)
        .should()
        .create(requestMapArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());

    Map<String, Object> requestMap = requestMapArgumentCaptor.getValue();

    assertThat(requestMap)
        .hasSize(4)
        .containsEntry("amount", amount)
        .containsEntry("currency", currency)
        .containsEntry("source", cardSource)
        .containsEntry("description", description);

    RequestOptions requestOptions = requestOptionsArgumentCaptor.getValue();

    assertThat(requestOptions).isNotNull();

    assertThat(cardPaymentCharge).isNotNull();
    assertThat(cardPaymentCharge.isCardDebited()).isTrue();
  }
}
