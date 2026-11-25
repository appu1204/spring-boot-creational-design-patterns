package com.finpay.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finpay.demo.dto.response.PaymentResponse;
import com.finpay.demo.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping ("api/payments")
@AllArgsConstructor 

public class PaymentController {

    PaymentService paymentService;

    @PostMapping("/without-singleton")
    public ResponseEntity<PaymentResponse> payWithoutSingleton(@RequestParam(defaultValue = "100.0") double amount) {
        return ResponseEntity.ok(paymentService.processWithoutSingleton(amount));
    }

    @PostMapping("/with-dcl")
    public ResponseEntity<PaymentResponse> payWithDcl(@RequestParam(defaultValue = "100.0") double amount) {
        return ResponseEntity.ok(paymentService.processWithDcl(amount));
    }

    @PostMapping("/with-billpugh")
    public ResponseEntity<PaymentResponse> payWithBillPugh(@RequestParam(defaultValue = "100.0") double amount) {
        return ResponseEntity.ok(paymentService.processWithBillPugh(amount));
    }

    @PostMapping("/simulate-load")
    public ResponseEntity<Map<String, Object>> simulateLoad(
            @RequestParam(defaultValue = "singleton") String mode,
            @RequestParam(defaultValue = "10") int count
    ) throws InterruptedException {
        return ResponseEntity.ok(paymentService.simulateConcurrentLoad(mode, count));
    }

}
