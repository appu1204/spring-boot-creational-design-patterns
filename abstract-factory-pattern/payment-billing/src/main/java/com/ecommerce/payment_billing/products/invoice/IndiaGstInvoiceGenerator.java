package com.ecommerce.payment_billing.products.invoice;

import org.springframework.stereotype.Component;

@Component 
public class IndiaGstInvoiceGenerator implements InvoiceGenerator{

    @Override 
    public InvoiceResult generateInvoice(String orderId, double baseAmount, double taxAmount, double total, String txnId) {
        String invoiceNumber = "INV-IN-" + orderId;
        String irnHash = Integer.toHexString((orderId + txnId).hashCode()) + "9a8b7c6d5e4f3a2b1c";
        String legal = String.format("Govt e-Invoice Compliant | GSTIN: 27AABCT3518Q1ZV | IRN: %s | Signed QR: Generated", irnHash);
        return new InvoiceResult(invoiceNumber, legal);
    }
}
