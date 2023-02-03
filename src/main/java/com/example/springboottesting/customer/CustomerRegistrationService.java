package com.example.springboottesting.customer;

import com.example.springboottesting.utils.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerRegistrationService {

  private final CustomerRepository customerRepository;
  private final PhoneNumberValidator phoneNumberValidator;

  public void registerNewCustomer(CustomerRegistrationRequest request) {
    String phoneNumber = request.customer().getPhoneNumber();
    boolean isValidPhoneNumber = phoneNumberValidator.test(phoneNumber);
    if (!isValidPhoneNumber) {
      throw new IllegalStateException("phone number [" + phoneNumber + "] is not valid");
    }
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
