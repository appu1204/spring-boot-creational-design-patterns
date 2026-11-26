package com.ecommerce.payment_billing.factory;

import org.springframework.stereotype.Component;

import com.ecommerce.payment_billing.products.invoice.InvoiceGenerator;
import com.ecommerce.payment_billing.products.invoice.UsReceiptGenerator;
import com.ecommerce.payment_billing.products.payment.PaymentGateway;
import com.ecommerce.payment_billing.products.payment.StripePaymentGateway;
import com.ecommerce.payment_billing.products.tax.TaxCalculator;
import com.ecommerce.payment_billing.products.tax.UsSalesTaxCalculator;

import lombok.RequiredArgsConstructor;

@Component("USA") 
@RequiredArgsConstructor 
public class UsaBillingFactory implements RegionalBillingFactory{

    private final UsSalesTaxCalculator taxCalculator;
    private final StripePaymentGateway paymentGateway;
    private final UsReceiptGenerator invoiceGenerator;

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
