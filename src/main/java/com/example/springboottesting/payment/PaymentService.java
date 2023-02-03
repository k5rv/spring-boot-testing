package com.example.springboottesting.payment;

import com.example.springboottesting.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.springboottesting.payment.Currency.GBP;
import static com.example.springboottesting.payment.Currency.USD;

@Service
@AllArgsConstructor
public class PaymentService {

  private static final List<Currency> ACCEPTED_CURRENCY = List.of(USD, GBP);
  private final CustomerRepository customerRepository;
  private final PaymentRepository paymentRepository;
  private final CardPaymentCharger cardPaymentCharger;

  void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
    boolean customerIsFound = customerRepository.findById(customerId).isPresent();
    if (!customerIsFound) {
      throw new IllegalStateException("Customer with id [" + customerId + "] not found");
    }

    Payment payment = paymentRequest.payment();
    boolean currencyIsSupported =
        ACCEPTED_CURRENCY.stream()
            .anyMatch(acceptedCurrency -> acceptedCurrency.equals(payment.getCurrency()));

    if (!currencyIsSupported) {
      throw new IllegalStateException(
          "Currency [ " + payment.getCurrency() + " ] is not supported");
    }

    CardPaymentCharge cardPaymentCharge =
        cardPaymentCharger.chargeCard(
            payment.getSource(),
            payment.getAmount(),
            payment.getCurrency(),
            payment.getDescription());

    boolean isCardDebited = cardPaymentCharge.isCardDebited();
    if (!isCardDebited) {
      throw new IllegalStateException("Card not debited for customer [" + customerId + "]");
    }

    payment.setCustomerId(customerId);
    paymentRepository.save(payment);
  }
}
