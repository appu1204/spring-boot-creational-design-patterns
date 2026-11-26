package com.ecommerce.payment_billing.factory;

import org.springframework.stereotype.Component;

import com.ecommerce.payment_billing.products.invoice.IndiaGstInvoiceGenerator;
import com.ecommerce.payment_billing.products.invoice.InvoiceGenerator;
import com.ecommerce.payment_billing.products.payment.PaymentGateway;
import com.ecommerce.payment_billing.products.payment.RazorpayPaymentGateway;
import com.ecommerce.payment_billing.products.tax.IndiaGstCalculator;
import com.ecommerce.payment_billing.products.tax.TaxCalculator;

import lombok.RequiredArgsConstructor;

@Component("INDIA")
@RequiredArgsConstructor 
public class IndiaBillingFactory implements RegionalBillingFactory {

    private final IndiaGstCalculator taxCalculator;
    private final RazorpayPaymentGateway paymentGateway;
    private final IndiaGstInvoiceGenerator invoiceGenerator;

    @Override
    public TaxCalculator createTaxCalculator() {
        return taxCalculator;
    }

    @Override
    public PaymentGateway createPaymentGateway() {
        return paymentGateway;
    }

    @Override
    public InvoiceGenerator createInvoiceGenerator() {
        return invoiceGenerator;
    }
}
