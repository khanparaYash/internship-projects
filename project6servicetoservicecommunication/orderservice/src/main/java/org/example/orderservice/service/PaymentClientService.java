package org.example.orderservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.orderservice.exception.ServiceUnavailableException;
import org.example.orderservice.repository.PaymentClient;
import org.springframework.stereotype.Service;

@Service
public class PaymentClientService {
    private final PaymentClient paymentClient;

    public PaymentClientService(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Retry(name = "paymentService")
    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")
    public String makePayment(Long productId) {
        System.out.println("Calling payment Service");
        return paymentClient.makePayment(productId);
    }

    public String fallback(Long productId, Exception ex) throws Exception {
        if (ex instanceof FeignException fe && fe.status() == 402) {
            throw fe; // Payment declined
        }
        throw new ServiceUnavailableException("payment service unavailable, please try again");
    }
}
