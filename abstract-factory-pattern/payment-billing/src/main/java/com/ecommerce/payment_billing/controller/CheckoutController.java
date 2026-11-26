package com.ecommerce.payment_billing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment_billing.dto.request.CheckoutRequest;
import com.ecommerce.payment_billing.dto.response.CheckoutResponse;
import com.ecommerce.payment_billing.service.CheckoutService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping ("api/billing")
@AllArgsConstructor 
public class CheckoutController {

    private CheckoutService checkoutService;

    @PostMapping("/pay-now")
    public ResponseEntity<CheckoutResponse> checkoutClean(@RequestBody CheckoutRequest request) {
        CheckoutResponse response = checkoutService.processWithFactory(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping ("noPattern/pay-now")
    public ResponseEntity<CheckoutResponse> checkoutFlawed(@RequestBody CheckoutRequest request) {
        CheckoutResponse response = checkoutService.processWithoutFactory(request);
        return ResponseEntity.ok(response);
    }
}
