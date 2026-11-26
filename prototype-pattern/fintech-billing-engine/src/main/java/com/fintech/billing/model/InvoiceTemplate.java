package com.fintech.billing.model;

import java.util.HashMap;
import java.util.Map;

public class InvoiceTemplate {

    // Heavy, shared fields
    private String merchantId;
    private String merchantName;
    private MerchantTaxProfile taxProfile;
    private BankRoutingDetails bankRouting;
    private Map<String, Double> forexRates;

    // Client-specific customized fields
    private String invoiceNumber;
    private String clientName;
    private String clientCompany;
    private String clientEmail;
    private String invoiceDescription;
    private double amount;
    private String currency;

    // Full constructor: Create master prototype
    public InvoiceTemplate(
            String merchantId,
            String merchantName,
            MerchantTaxProfile taxProfile,
            BankRoutingDetails bankRouting,
            Map<String, Double> forexRates) {

        this.merchantId = merchantId;
        this.merchantName = merchantName;

        // Defensive copying
        this.taxProfile = (taxProfile != null) ? new MerchantTaxProfile(taxProfile) : null;
        this.bankRouting = (bankRouting != null) ? new BankRoutingDetails(bankRouting) : null;
        this.forexRates = (forexRates != null) ? new HashMap<>(forexRates) : new HashMap<>();
    }

    // Deep copy constructor: The heart of Prototype Pattern
    public InvoiceTemplate(InvoiceTemplate source) {
        this.merchantId = source.merchantId;
        this.merchantName = source.merchantName;

        // Deep copy nested mutable objects
        this.taxProfile = (source.taxProfile != null) ? new MerchantTaxProfile(source.taxProfile) : null;
        this.bankRouting = (source.bankRouting != null) ? new BankRoutingDetails(source.bankRouting) : null;
        this.forexRates = new HashMap<>(source.forexRates);

        // Client-specific fields start fresh
        this.invoiceNumber = null;
        this.clientName = null;
        this.clientCompany = null;
        this.clientEmail = null;
        this.invoiceDescription = null;
        this.amount = 0.0;
        this.currency = null;
    }

    // ⚡ The Prototype Pattern clone method
    public InvoiceTemplate clone() {
        return new InvoiceTemplate(this);
    }

    // Getters for shared fields
    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    // Return defensive copies of mutable objects
    public MerchantTaxProfile getTaxProfile() {
        return (taxProfile != null) ? new MerchantTaxProfile(taxProfile) : null;
    }

    public BankRoutingDetails getBankRouting() {
        return (bankRouting != null) ? new BankRoutingDetails(bankRouting) : null;
    }

    public Map<String, Double> getForexRates() {
        return new HashMap<>(forexRates);
    }

    // Client-specific getters and setters
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}