package com.example.springboottesting.customer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Service
@Validated
@AllArgsConstructor
public class CustomerRegistrationService {

  private final CustomerRepository customerRepository;

  public void registerNewCustomer(@Valid CustomerRegistrationRequest request) {
    String phoneNumber = request.customer().getPhoneNumber();
    Optional<Customer> customerOptional =
        customerRepository.selectCustomerByPhoneNumber(phoneNumber);
    if (customerOptional.isPresent()) {
      if (customerOptional.get().getName().equals(request.customer().getName())) {
        return;
      }
      throw new IllegalStateException("phone number [" + phoneNumber + "] is taken");
    }
    if (request.customer().getId() == null) {
      request.customer().setId(UUID.randomUUID());
    }
    customerRepository.save(request.customer());
  }
}
