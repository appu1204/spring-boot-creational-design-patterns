
package com.fintech.billing.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.fintech.billing.model.BankRoutingDetails;
import com.fintech.billing.model.InvoiceTemplate;
import com.fintech.billing.model.MerchantTaxProfile;
import com.fintech.billing.service.ExternalFintechClientSimulator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoicePrototypeRegistry {

    private final ExternalFintechClientSimulator externalSimulator;

    private final Map<String, InvoiceTemplate> prototypes = new ConcurrentHashMap<>();

    // Load master templates once at startup
    @PostConstruct
    public void init() {

        System.out.println(">>> Initializing Master Invoice Prototypes...");

        String merchantId = "MERCHANT_ACME_CORP";

        MerchantTaxProfile taxProfile =
                externalSimulator.fetchTaxProfileFromVault(merchantId);

        Map<String, Double> forex =
                externalSimulator.fetchForexRatesFromBank();

        BankRoutingDetails bank =
                externalSimulator.fetchBankRoutingFromCoreBanking(merchantId);

        InvoiceTemplate masterTemplate = new InvoiceTemplate(
                merchantId,
                "Acme Cloud Corporation",
                taxProfile,
                bank,
                forex
        );

        prototypes.put(merchantId, masterTemplate);

        System.out.println(">>> Master Prototype successfully cached!");
    }

    // Return a fresh independent copy
    public InvoiceTemplate getInvoiceClone(String merchantId) {

        InvoiceTemplate master = prototypes.get(merchantId);

        if (master == null) {
            throw new IllegalArgumentException(
                    "No prototype registered for merchant: " + merchantId
            );
        }

        return new InvoiceTemplate(master);
    }
}
