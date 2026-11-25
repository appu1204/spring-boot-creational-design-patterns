package com.merchant.payment_service.dto.request;

public record PaymentRequest(
    String orderId,
    double amount,
    String currency,
    String paymentMethod,
    String customerEmail
) {}
