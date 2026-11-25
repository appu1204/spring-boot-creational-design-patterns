package com.merchant.payment_service.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.merchant.payment_service.processor.PaymentProcessor;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PaymentProcessorFactory {

    private final ApplicationContext applicationContext;

    public PaymentProcessor getProcessor(String paymentMethod) {

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty.");
        }

        try {
            // THE FACTORY SHIELD:
            // Only resolves and instantiates the specific requested processor.
            // If paying with STRIPE, CryptoProcessor is NEVER touched!
            return applicationContext.getBean(paymentMethod.trim().toUpperCase(), PaymentProcessor.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Payment method '" + paymentMethod + "' is unavailable or failed: " + e.getMessage()
            );
        }
    }
}