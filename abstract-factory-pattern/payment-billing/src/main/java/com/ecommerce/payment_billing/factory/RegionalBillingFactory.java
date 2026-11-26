package com.ecommerce.payment_billing.factory;

import com.ecommerce.payment_billing.products.invoice.InvoiceGenerator;
import com.ecommerce.payment_billing.products.payment.PaymentGateway;
import com.ecommerce.payment_billing.products.tax.TaxCalculator;

public interface RegionalBillingFactory {

    TaxCalculator createTaxCalculator();
    PaymentGateway createPaymentGateway();
    InvoiceGenerator createInvoiceGenerator();

}
