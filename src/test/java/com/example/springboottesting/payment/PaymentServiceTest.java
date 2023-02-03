package com.example.springboottesting.payment;

import com.example.springboottesting.customer.Customer;
import com.example.springboottesting.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.example.springboottesting.payment.Currency.EUR;
import static com.example.springboottesting.payment.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class PaymentServiceTest {

  @Mock private CustomerRepository customerRepository;

  @Mock private PaymentRepository paymentRepository;

  @Mock private CardPaymentCharger cardPaymentCharger;

  private PaymentService underTest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    underTest = new PaymentService(customerRepository, paymentRepository, cardPaymentCharger);
  }

  @Test
  void itShouldChargeCardSuccessfully() {
    // Given
    UUID customerId = UUID.randomUUID();
    given(customerRepository.findById(customerId))
        .willReturn(Optional.of(Mockito.mock(Customer.class)));

    PaymentRequest paymentRequest =
        new PaymentRequest(
            new Payment(null, customerId, new BigDecimal("100.00"), USD, "card123xx", "Donation"));

    Payment payment = paymentRequest.payment();

    given(
            cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()))
        .willReturn(new CardPaymentCharge(true));

    // When
    underTest.chargeCard(customerId, paymentRequest);

    // Then
    ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
    then(paymentRepository).should().save(paymentArgumentCaptor.capture());

    Payment paymentArgumentCaptureValue = paymentArgumentCaptor.getValue();
    assertThat(paymentArgumentCaptureValue)
        .usingRecursiveComparison()
        .ignoringFields("customerId")
        .isEqualTo(payment);
    assertThat(paymentArgumentCaptureValue.getCustomerId()).isEqualTo(customerId);
  }

  @Test
  void itShouldThrowWhenCardIsNotChargeCard() {
    // Given
    UUID customerId = UUID.randomUUID();
    given(customerRepository.findById(customerId))
        .willReturn(Optional.of(Mockito.mock(Customer.class)));

    PaymentRequest paymentRequest =
        new PaymentRequest(
            new Payment(null, customerId, new BigDecimal("100.00"), USD, "card123xx", "Donation"));

    given(
            cardPaymentCharger.chargeCard(
                paymentRequest.payment().getSource(),
                paymentRequest.payment().getAmount(),
                paymentRequest.payment().getCurrency(),
                paymentRequest.payment().getDescription()))
        .willReturn(new CardPaymentCharge(false));

    // When and Then
    assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Card not debited for customer [" + customerId + "]");
    then(paymentRepository).should(never()).save(any(Payment.class));
  }

  @Test
  void itShouldNotChargeCardAndThrowWhenCurrencyNotSupported() {
    // Given
    UUID customerId = UUID.randomUUID();
    given(customerRepository.findById(customerId))
        .willReturn(Optional.of(Mockito.mock(Customer.class)));

    PaymentRequest paymentRequest =
        new PaymentRequest(
            new Payment(null, customerId, new BigDecimal("100.00"), EUR, "card123xx", "Donation"));

    // When and Then
    assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Currency [ " + paymentRequest.payment().getCurrency() + " ] is not supported");

    then(cardPaymentCharger).shouldHaveNoInteractions();

    then(paymentRepository).shouldHaveNoInteractions();
  }

  @Test
  void itShouldNotChargeCardAndThrowWhenCustomerNotFound() {
    // Given
    UUID customerId = UUID.randomUUID();

    // When and Then
    assertThatThrownBy(() -> underTest.chargeCard(customerId, new PaymentRequest(new Payment())))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Customer with id [" + customerId + "] not found");

    then(cardPaymentCharger).shouldHaveNoInteractions();

    then(paymentRepository).shouldHaveNoInteractions();
  }
}
