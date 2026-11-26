package com.ecommerce.payment_billing.dto.request;

import com.ecommerce.payment_billing.model.Region;

public record CheckoutRequest(
    String orderId,
    String customerId,
    double amount,
    Region region
) {}
