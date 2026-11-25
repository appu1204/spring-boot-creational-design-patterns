package com.merchant.payment_service.processor;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Component ("PAYPAL")
@Slf4j 
public class PayPalProcessor implements PaymentProcessor{

    @Override 
    public PaymentResponse process(PaymentRequest request) {
        log.info("Capturing funds: {} {} for order {}",
                request.currency(),
                request.amount(),
                request.orderId()
        );

        return new PaymentResponse(
            "SUCCESS",
            "pp_cap_" + UUID.randomUUID().toString().substring(0, 10),
            request.amount(),
            request.currency(),
            "PAYPAL",
            "Funds captured successfully via PayPal Wallet",
            Instant.now()
        );
    }
}
