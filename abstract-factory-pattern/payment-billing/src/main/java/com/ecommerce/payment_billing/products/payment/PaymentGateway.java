package com.ecommerce.payment_billing.products.payment;

public interface PaymentGateway {

    PaymentResult charge(double amount);

    record PaymentResult(
        String transactionId,
        String gatewayName,
        String currency
    ){}
}
