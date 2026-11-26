package com.ecommerce.payment_billing.dto.response;

import com.ecommerce.payment_billing.model.Region;

public record CheckoutResponse(
    String orderId,
    Region region,
    String currency,
    double baseAmount,
    double taxAmount,
    String taxBreakdown,
    double totalAmountPaid,
    String paymentTransactionId,
    String paymentGateway,
    String invoiceNumber,
    String invoiceLegalDetails,
    String complianceStatus
) {}
