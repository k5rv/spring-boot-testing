package com.example.springboottesting.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
  @Id @GeneratedValue private Long paymentId;
  private UUID customerId;
  private BigDecimal amount;
  private Currency currency;
  private String source;
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Payment payment = (Payment) o;
    return paymentId.equals(payment.paymentId)
        && customerId.equals(payment.customerId)
        && amount.equals(payment.amount)
        && currency == payment.currency
        && source.equals(payment.source)
        && description.equals(payment.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentId, customerId, amount, currency, source, description);
  }
}
