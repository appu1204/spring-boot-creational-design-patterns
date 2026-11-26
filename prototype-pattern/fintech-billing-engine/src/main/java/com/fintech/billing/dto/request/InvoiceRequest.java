package com.fintech.billing.dto.request;

public record InvoiceRequest(
    String merchantId,
    String clientName,
    String clientCompany,
    String clientEmail,
    String invoiceDescription,
    double amount,
    String currency
) {}
