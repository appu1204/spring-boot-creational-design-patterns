package com.fintech.billing.model;

import java.util.List;

public class MerchantTaxProfile {
    private String taxRegistrationNumber;
    private String digitalCertificateSignature;
    private List<String> legalDisclaimers;

    public MerchantTaxProfile(String taxNumber, String digitalCert, List<String> disclaimers) {
        this.taxRegistrationNumber = taxNumber;
        this.digitalCertificateSignature = digitalCert;
        this.legalDisclaimers = disclaimers;
    }

    public MerchantTaxProfile(MerchantTaxProfile source) {
        this.taxRegistrationNumber = source.taxRegistrationNumber;
        this.digitalCertificateSignature = source.digitalCertificateSignature;
        this.legalDisclaimers = source.legalDisclaimers;
    }

    public String getTaxRegistrationNumber() { 
        return taxRegistrationNumber; 
    }

    public String getCombinedLegalTerms() { 
        return String.join(" | ", legalDisclaimers); 
    }
    
}
