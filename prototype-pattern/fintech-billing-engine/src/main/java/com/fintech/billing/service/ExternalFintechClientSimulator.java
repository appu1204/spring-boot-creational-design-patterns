package com.fintech.billing.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fintech.billing.model.BankRoutingDetails;
import com.fintech.billing.model.MerchantTaxProfile;

@Service 
public class ExternalFintechClientSimulator {

    public MerchantTaxProfile fetchTaxProfileFromVault(String merchantId) {
        simulateNetworkLatency(300);
        List<String> legal = Arrays.asList(
            "Net 30 days payment term.",
            "Subject to Delaware corporate financial arbitration.",
            "Authorized e-signature certificate #99281-HSM"
        );
        return new MerchantTaxProfile("VAT-US-9988221", "SHA256_CERT_ACME_VALID", legal);
    }
    // Simulates calling external Forex Currency API (~400ms)
    public Map<String, Double> fetchForexRatesFromBank() {
        simulateNetworkLatency(400);
        Map<String, Double> rates = new HashMap<>();
        rates.put("INR", 83.00);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.78);
        return rates;
    }
    // Simulates querying Core Banking Gateway DB (~300ms)
    public BankRoutingDetails fetchBankRoutingFromCoreBanking(String merchantId) {
        simulateNetworkLatency(300);
        return new BankRoutingDetails("JPMorgan Chase NY", "CHASUS33XXX", "US893704004405112");
    }
    private void simulateNetworkLatency(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
