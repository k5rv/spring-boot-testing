package com.example.springboottesting.payment;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
@AllArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public void makePayment(@RequestBody PaymentRequest paymentRequest) {
    paymentService.chargeCard(paymentRequest.payment().getCustomerId(), paymentRequest);
  }
}
