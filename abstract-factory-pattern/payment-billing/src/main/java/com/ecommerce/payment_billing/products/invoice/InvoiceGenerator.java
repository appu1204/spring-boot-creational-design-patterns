package com.ecommerce.payment_billing.products.invoice;

public interface InvoiceGenerator {
    InvoiceResult generateInvoice(String orderId, double baseAmount, double taxAmount, double total, String txnId);

    record InvoiceResult(
        String invoiceNumber,
        String legalDetails
    ){}
}
