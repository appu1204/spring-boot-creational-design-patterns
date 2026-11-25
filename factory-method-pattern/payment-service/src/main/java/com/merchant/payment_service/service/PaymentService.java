
package com.merchant.payment_service.service;

import org.springframework.stereotype.Service;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;
import com.merchant.payment_service.factory.PaymentProcessorFactory;
import com.merchant.payment_service.processor.PaymentProcessor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentProcessorFactory paymentProcessorFactory;

    public PaymentResponse processPayment(PaymentRequest request) {

        // Step 1: Business rule checks
        if (request.amount() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero.");
        }

        log.info("Starting payment workflow for order: {}",
                request.orderId());

        // Step 2: Resolve the correct processor
        PaymentProcessor processor =
                paymentProcessorFactory.getProcessor(request.paymentMethod());

        // Step 3: Execute payment
        PaymentResponse response = processor.process(request);

        // Step 4: Post-processing
        if ("SUCCESS".equalsIgnoreCase(response.status())) {
            recordAuditLog(request, response);
            sendReceiptEmail(request.customerEmail(), response);
        }

        return response;
    }

    private void recordAuditLog(
            PaymentRequest request, PaymentResponse response) {

        log.info("Saved transaction {} for order {} to database",
                response.transactionId(),
                request.orderId());
    }

    private void sendReceiptEmail(
            String email, PaymentResponse response) {

        if (email != null && !email.isEmpty()) {
            log.info("Receipt sent to {} for amount: {} {}",
                    email,
                    response.amount(),
                    response.currency());
        }
    }
}
