package com.ecommerce.payment_billing.service;

import org.springframework.stereotype.Service;

import com.ecommerce.payment_billing.dto.request.CheckoutRequest;
import com.ecommerce.payment_billing.dto.response.CheckoutResponse;
import com.ecommerce.payment_billing.factory.BillingFactoryProvider;
import com.ecommerce.payment_billing.factory.RegionalBillingFactory;
import com.ecommerce.payment_billing.products.invoice.InvoiceGenerator;
import com.ecommerce.payment_billing.products.invoice.UsReceiptGenerator;
import com.ecommerce.payment_billing.products.payment.PaymentGateway;
import com.ecommerce.payment_billing.products.payment.RazorpayPaymentGateway;
import com.ecommerce.payment_billing.products.tax.TaxCalculator;
import com.ecommerce.payment_billing.products.tax.UsSalesTaxCalculator;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CheckoutService {

    private final BillingFactoryProvider factoryProvider;

    public CheckoutResponse processWithFactory(CheckoutRequest request) {
        RegionalBillingFactory factory =
                factoryProvider.getFactory(request.region());

        TaxCalculator taxCalculator = factory.createTaxCalculator();
        PaymentGateway paymentGateway = factory.createPaymentGateway();
        InvoiceGenerator invoiceGenerator = factory.createInvoiceGenerator();

        TaxCalculator.TaxResult taxResult =
                taxCalculator.calculateTax(request.amount());

        double totalToCharge = request.amount() + taxResult.taxAmount();

        PaymentGateway.PaymentResult paymentResult =
                paymentGateway.charge(totalToCharge);

        InvoiceGenerator.InvoiceResult invoiceResult =
                invoiceGenerator.generateInvoice(
                        request.orderId(),
                        request.amount(),
                        taxResult.taxAmount(),
                        totalToCharge,
                        paymentResult.transactionId()
                );

        return new CheckoutResponse(
            request.orderId(),
            request.region(),
            paymentResult.currency(),
            request.amount(),
            taxResult.taxAmount(),
            taxResult.breakdown(),
            totalToCharge,
            paymentResult.transactionId(),
            paymentResult.gatewayName(),
            invoiceResult.invoiceNumber(),
            invoiceResult.legalDetails(),
            "100% COMPLIANT - ALL REGIONAL COMPONENTS MATCHED");
    }

    public CheckoutResponse processWithoutFactory(CheckoutRequest request) {
        // Instantiate directly with 'new' to simulate the loose/uncoordinated anti-pattern
        PaymentGateway paymentGateway = new RazorpayPaymentGateway();
        TaxCalculator taxCalculator = new UsSalesTaxCalculator();
        InvoiceGenerator invoiceGenerator = new UsReceiptGenerator();

        TaxCalculator.TaxResult taxResult =
                taxCalculator.calculateTax(request.amount());

        double totalToCharge = request.amount() + taxResult.taxAmount();

        PaymentGateway.PaymentResult paymentResult =
                paymentGateway.charge(totalToCharge);

        InvoiceGenerator.InvoiceResult invoiceResult =
                invoiceGenerator.generateInvoice(
                        request.orderId(),
                        request.amount(),
                        taxResult.taxAmount(),
                        totalToCharge,
                        paymentResult.transactionId()
                );

        return new CheckoutResponse(
                request.orderId(),
                request.region(), 
                paymentResult.currency(),
                request.amount(),
                taxResult.taxAmount(),
                taxResult.breakdown(),
                totalToCharge,
                paymentResult.transactionId(),
                paymentResult.gatewayName(),
                invoiceResult.invoiceNumber(),
                invoiceResult.legalDetails(),
                "NON-COMPLIANT: TAX & INVOICE REGION MISMATCH DETECTED!"
        );
    }
}