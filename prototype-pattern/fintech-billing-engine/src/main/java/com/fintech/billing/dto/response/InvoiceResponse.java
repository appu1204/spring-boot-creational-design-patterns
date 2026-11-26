package com.fintech.billing.dto.response;

public record InvoiceResponse(
    String invoiceNumber,
        String merchantName,
        String clientName,
        double billingAmount,
        String currency,
        String forexConvertedAmount,
        String taxRegistrationNumber,
        String paymentWireRouting,
        String legalTerms,
        long executionTimeMs,
        String strategyUsed
) {}
