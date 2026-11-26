package com.ecommerce.payment_billing.products.tax;

import org.springframework.stereotype.Component;

@Component 
public class IndiaGstCalculator implements TaxCalculator{

    @Override 
    public TaxResult calculateTax(double baseAmount) {
        double cgst = baseAmount * 0.09;
        double sgst = baseAmount * 0.09;
        double totalGst = cgst + sgst;
        String breakdown = String.format("Indian GST 18%% (CGST: ₹%.2f, SGST: ₹%.2f)", cgst, sgst);
        return new TaxResult(totalGst, breakdown);
    }

}
