package com.merchant.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;
import com.merchant.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;


@RestController 
@RequestMapping ("api/payments")
@AllArgsConstructor 
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("/pay-now")
    public ResponseEntity<PaymentResponse> payNow(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("Payment Error: " + ex.getMessage());
    }
}
