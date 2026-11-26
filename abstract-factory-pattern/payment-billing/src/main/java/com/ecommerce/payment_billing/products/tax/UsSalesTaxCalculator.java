package com.ecommerce.payment_billing.products.tax;

import org.springframework.stereotype.Component;

@Component 
public class UsSalesTaxCalculator implements TaxCalculator{

    @Override 
    public TaxResult calculateTax(double baseAmount) {
        double salesTax = baseAmount * 0.0825;
        String breakdown = String.format("California Sales Tax 8.25%% ($%.2f)", salesTax);
        return new TaxResult(salesTax, breakdown);
    }

}
