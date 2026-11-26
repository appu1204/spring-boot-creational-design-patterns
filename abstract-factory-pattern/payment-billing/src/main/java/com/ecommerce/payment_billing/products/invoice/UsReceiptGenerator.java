package com.ecommerce.payment_billing.products.invoice;

import org.springframework.stereotype.Component;

@Component 
public class UsReceiptGenerator implements InvoiceGenerator{

    @Override 
    public InvoiceResult generateInvoice(String orderId, double baseAmount, double taxAmount, double total, String txnId) {
        String invoiceNumber = "REC-US-" + orderId;
        String legal = "US IRS Standard Commercial Receipt | Merchant EIN: 12-3456789 | Sales Tax collected under CA Reg #9876543";
        return new InvoiceResult(invoiceNumber, legal);
       
    }

}
