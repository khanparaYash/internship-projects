package org.example.paymentservice.controller;
import com.example.api.PaymentApi;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }

    @Override
    public ResponseEntity<String> makePayment( Long orderId) throws InterruptedException {
        return ResponseEntity.ok(paymentService.makePayment(orderId));
    }
}
