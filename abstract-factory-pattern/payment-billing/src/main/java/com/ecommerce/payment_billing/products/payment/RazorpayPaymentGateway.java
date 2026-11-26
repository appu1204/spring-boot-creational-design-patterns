package com.ecommerce.payment_billing.products.payment;

import org.springframework.stereotype.Component;

@Component 
public class RazorpayPaymentGateway implements PaymentGateway{

    @Override 
    public PaymentResult charge(double amount) {
        String txnId = "pay_rzp_" + System.currentTimeMillis();
        return new PaymentResult(txnId, "Razorpay (India UPI/Netbanking/Cards)", "INR");
    }

}
