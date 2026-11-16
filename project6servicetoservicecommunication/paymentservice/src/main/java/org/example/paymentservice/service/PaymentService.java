package org.example.paymentservice.service;

import org.example.paymentservice.entity.Payment;
import org.example.paymentservice.entity.PaymentStatus;
import org.example.paymentservice.exception.PaymentFailedException;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    @Transactional
    public String makePayment(Long orderId) throws InterruptedException {
//        Thread.sleep(6000);
        boolean isPaid = ThreadLocalRandom.current().nextBoolean();
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        if (isPaid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
            return "SUCCESS";
        }
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        throw new PaymentFailedException("Payment was declined");
    }
}
