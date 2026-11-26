package com.ecommerce.payment_billing.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ecommerce.payment_billing.model.Region;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class BillingFactoryProvider {
    private final Map<String, RegionalBillingFactory> factories;
    
    public RegionalBillingFactory getFactory(Region region) {
        RegionalBillingFactory factory = factories.get(region.name());

        if (factory == null) {
            throw new IllegalArgumentException("Unsupported billing region: " + region);
        }
        return factory;
    }
}
