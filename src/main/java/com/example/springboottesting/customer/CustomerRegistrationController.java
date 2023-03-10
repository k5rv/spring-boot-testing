package com.example.springboottesting.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer-registration")
@AllArgsConstructor
public class CustomerRegistrationController {

  private final CustomerRegistrationService customerRegistrationService;

  @PutMapping
  public void registerNewCustomer(
      @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
    customerRegistrationService.registerNewCustomer(customerRegistrationRequest);
  }
}
