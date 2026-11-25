package com.merchant.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;
import com.merchant.payment_service.processor.CryptoProcessor;
import com.merchant.payment_service.processor.PayPalProcessor;
import com.merchant.payment_service.processor.StripeProcessor;

@RestController
@RequestMapping("api/payments/naive")
public class NaivePaymentController {

    @PostMapping("/pay-now")
    public ResponseEntity<?> payNow(@RequestBody PaymentRequest request) {
        System.out.println("Eagerly instantiating all payment processors...");

        /**
         * Here this is tight coupling
         */
        StripeProcessor stripeProcessor = new StripeProcessor();
        PayPalProcessor payPalProcessor = new PayPalProcessor();
        CryptoProcessor cryptoProcessor = new CryptoProcessor(); // here hardcode so that this will fail

        if ("STRIPE".equalsIgnoreCase(request.paymentMethod())) {
            PaymentResponse response = stripeProcessor.process(request);
            return ResponseEntity.ok(response);
        } else if ("PAYPAL".equalsIgnoreCase(request.paymentMethod())) {
            PaymentResponse response = payPalProcessor.process(request);
            return ResponseEntity.ok(response);
        } else if ("CRYPTO".equalsIgnoreCase(request.paymentMethod())) {
            PaymentResponse response = cryptoProcessor.process(request);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body("Unsupported payment method: " + request.paymentMethod());
    }
}