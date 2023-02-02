package com.example.springboottesting.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
class PaymentRepositoryTest {

  @Autowired private PaymentRepository underTest;

  @Test
  void itShouldInsertPayment() {
    // Given
    long paymentId = 1L;
    Payment payment =
        new Payment(
            paymentId,
            UUID.randomUUID(),
            new BigDecimal("10.00"),
            Currency.USD,
            "card123",
            "Donation");
    // When
    underTest.save(payment);
    // Then
    Optional<Payment> paymentOptional = underTest.findById(paymentId);
    Assertions.assertThat(paymentOptional)
        .isPresent()
        .hasValueSatisfying(p -> Assertions.assertThat(p).isEqualTo(payment));
  }
}
