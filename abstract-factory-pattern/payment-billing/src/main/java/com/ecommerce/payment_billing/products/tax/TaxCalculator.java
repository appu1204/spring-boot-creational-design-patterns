package com.ecommerce.payment_billing.products.tax;

public interface TaxCalculator {

    TaxResult calculateTax(double baseAmount);

    public record TaxResult(
        double taxAmount,
        String breakdown
    ) {}
}
