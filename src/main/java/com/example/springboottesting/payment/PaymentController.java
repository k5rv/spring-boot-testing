package com.example.springboottesting.payment;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
@AllArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PutMapping("/{customerId}")
  public void makePayment(
      @PathVariable UUID customerId, @RequestBody PaymentRequest paymentRequest) {
    paymentService.chargeCard(customerId, paymentRequest);
  }
}
