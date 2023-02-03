package com.example.springboottesting.payment;

import com.example.springboottesting.customer.Customer;
import com.example.springboottesting.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static com.example.springboottesting.payment.Currency.GBP;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {

  @Autowired PaymentRepository paymentRepository;

  @Autowired private MockMvc mockMvc;

  @Test
  void itShouldCreatePaymentSuccessfully() throws Exception {
    // Given
    UUID customerId = UUID.randomUUID();
    Customer customer = new Customer(customerId, "James", "+44700000000");

    CustomerRegistrationRequest customerRegistrationRequest =
        new CustomerRegistrationRequest(customer);

    ResultActions customerRegistrationResultActions =
        mockMvc.perform(
            put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))));

    long paymentId = 1L;
    Payment payment =
        new Payment(paymentId, customerId, new BigDecimal("100.00"), GBP, "x0x0x0x0", "Donation");

    PaymentRequest paymentRequest = new PaymentRequest(payment);

    // When
    ResultActions paymentResultActions =
        mockMvc.perform(
            post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));

    // Then
    customerRegistrationResultActions.andExpect(status().isOk());
    paymentResultActions.andExpect(status().isOk());

    Assertions.assertThat(paymentRepository.findById(paymentId))
        .isPresent()
        .hasValueSatisfying(
            p -> Assertions.assertThat(p).usingRecursiveComparison().isEqualTo(payment));
  }

  private String objectToJson(Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      AssertionErrors.fail("Fail to convert object [" + object + "] to Json");
      return null;
    }
  }
}
