package com.merchant.payment_service.processor;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Component("STRIPE")
@Slf4j
public class StripeProcessor implements PaymentProcessor {

    @Override
    public PaymentResponse process(PaymentRequest request) {

        log.info("Charging {} {} for order {}",
                request.currency(),
                request.amount(),
                request.orderId());

        return new PaymentResponse(
            "SUCCESS",
            "ch_stripe_" + UUID.randomUUID().toString().substring(0, 10),
            request.amount(),
            request.currency(),
            "STRIPE",
            "Card charged successfully via Stripe Gateway",
            Instant.now()
        );
    }
}