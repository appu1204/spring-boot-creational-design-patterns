package com.merchant.payment_service.dto.response;

import java.time.Instant;

public record PaymentResponse(
    String status,
    String transactionId,
    double amount,
    String currency,
    String paymentMethod,
    String message,
    Instant timestamp
) {}
