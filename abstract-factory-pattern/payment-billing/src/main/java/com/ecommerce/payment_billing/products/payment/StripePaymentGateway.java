package com.ecommerce.payment_billing.products.payment;

import org.springframework.stereotype.Component;

@Component 
public class StripePaymentGateway implements PaymentGateway{

    @Override 
    public PaymentResult charge(double amount) {
        String txnId = "ch_stripe_" + System.currentTimeMillis();
        return new PaymentResult(txnId, "Stripe (US ACH/Credit Cards)", "USD");
        
    }

}
