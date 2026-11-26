package com.fintech.billing.service;

import com.fintech.billing.dto.request.InvoiceRequest;
import com.fintech.billing.dto.response.InvoiceResponse;
import com.fintech.billing.model.BankRoutingDetails;
import com.fintech.billing.model.InvoiceTemplate;
import com.fintech.billing.model.MerchantTaxProfile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class NaiveInvoiceService {

    private final ExternalFintechClientSimulator externalSimulator;

    public NaiveInvoiceService(ExternalFintechClientSimulator externalSimulator) {
        this.externalSimulator = externalSimulator;
    }

    public InvoiceResponse createInvoiceNaive(InvoiceRequest request) {
        long startTime = System.currentTimeMillis();

        // 1. Slow Vault Call (300ms)
        MerchantTaxProfile taxProfile = externalSimulator.fetchTaxProfileFromVault(request.merchantId());

        // 2. Slow Forex API Call (400ms)
        Map<String, Double> forex = externalSimulator.fetchForexRatesFromBank();

        // 3. Slow Banking DB Call (300ms)
        BankRoutingDetails bank = externalSimulator.fetchBankRoutingFromCoreBanking(request.merchantId());

        // Assemble invoice from scratch
        InvoiceTemplate invoice = new InvoiceTemplate(
                request.merchantId(),
                "Acme Cloud Corporation",
                taxProfile,
                bank,
                forex
        );

        // Customize with client data
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setClientName(request.clientName());
        invoice.setClientCompany(request.clientCompany());
        invoice.setClientEmail(request.clientEmail());
        invoice.setInvoiceDescription(request.invoiceDescription());
        invoice.setAmount(request.amount());
        invoice.setCurrency(request.currency());

        long executionTime = System.currentTimeMillis() - startTime;

        return buildResponse(invoice, executionTime, "WITHOUT_PROTOTYPE (Reconstructed from scratch)");
    }

    private InvoiceResponse buildResponse(
        InvoiceTemplate invoice,
        long timeMs,
        String strategy) {
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