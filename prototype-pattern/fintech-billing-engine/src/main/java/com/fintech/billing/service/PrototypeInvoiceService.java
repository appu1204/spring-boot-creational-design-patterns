package com.fintech.billing.service;

import com.fintech.billing.dto.request.InvoiceRequest;
import com.fintech.billing.dto.response.InvoiceResponse;
import com.fintech.billing.model.InvoiceTemplate;
import com.fintech.billing.registry.InvoicePrototypeRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrototypeInvoiceService {

    private final InvoicePrototypeRegistry prototypeRegistry;

    public PrototypeInvoiceService(InvoicePrototypeRegistry prototypeRegistry) {
        this.prototypeRegistry = prototypeRegistry;
    }

    public InvoiceResponse createInvoiceWithPrototype(InvoiceRequest request) {

        long startTime = System.currentTimeMillis();

        // Instant in-memory clone
        InvoiceTemplate invoice =
                prototypeRegistry.getInvoiceClone(request.merchantId());

        // Customize with client data
        invoice.setInvoiceNumber(
                "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );

        invoice.setClientName(request.clientName());
        invoice.setClientCompany(request.clientCompany());
        invoice.setClientEmail(request.clientEmail());
        invoice.setInvoiceDescription(request.invoiceDescription());
        invoice.setAmount(request.amount());
        invoice.setCurrency(request.currency());

        long executionTime = System.currentTimeMillis() - startTime;

        return buildResponse(
                invoice,
                executionTime,
                "WITH_PROTOTYPE (Cloned from In-Memory Master)"
        );
    }

    private InvoiceResponse buildResponse(
            InvoiceTemplate invoice,
            long timeMs,
            String strategy
    ) {

        double inrRate = invoice.getForexRates().getOrDefault("INR", 83.0);

        double converted = invoice.getAmount() * inrRate;

        String forexConvertedAmount =
                "INR " + String.format("%.2f", converted)
                + " (Rate: " + inrRate + ")";

        return new InvoiceResponse(
                invoice.getInvoiceNumber(),
                invoice.getMerchantName(),
                invoice.getClientName() + " (" + invoice.getClientCompany() + ")",
                invoice.getAmount(),
                invoice.getCurrency(),
                forexConvertedAmount,
                invoice.getTaxProfile().getTaxRegistrationNumber(),
                invoice.getBankRouting().getFormattedRouting(),
                invoice.getTaxProfile().getCombinedLegalTerms(),
                timeMs,
                strategy
        );
    }
}