package org.example.orderservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("PAYMENTSERVICE")
public interface PaymentClient {
    @PostMapping("/api/v1/payment")
    String makePayment(@RequestBody Long orderId);
}
