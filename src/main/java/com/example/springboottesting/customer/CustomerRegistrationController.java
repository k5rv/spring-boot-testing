package com.example.springboottesting.customer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer-registration")
@AllArgsConstructor
public class CustomerRegistrationController {

  private final CustomerRegistrationService registrationService;

  @PutMapping
  public void registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
    // registrationService.registerNewCustomer(null);
  }
}
