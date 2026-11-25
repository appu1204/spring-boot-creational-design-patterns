package com.merchant.payment_service.processor;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;

public interface PaymentProcessor {
    PaymentResponse process(PaymentRequest request);
}
