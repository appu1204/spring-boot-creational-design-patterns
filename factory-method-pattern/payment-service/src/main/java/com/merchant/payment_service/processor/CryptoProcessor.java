package com.merchant.payment_service.processor;

import java.time.Instant;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.merchant.payment_service.dto.request.PaymentRequest;
import com.merchant.payment_service.dto.response.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Component ("CRYPTO")
@Slf4j 
@Lazy 

public class CryptoProcessor implements PaymentProcessor{

    public CryptoProcessor() {
        // FAILURE:
        log.error("Failed to connect to Ethereum Node at https://mainnet.infura.io");
        throw new RuntimeException("CRITICAL: Blockchain node connection refused! Crypto gateway offline.");
    }

    @Override
    public PaymentResponse process(PaymentRequest request) {

        log.info("Broadcasting transaction for order {} on-chain",
                request.orderId());

        return new PaymentResponse(
            "SUCCESS",
            "0x" + UUID.randomUUID().toString().replace("-", ""),
            request.amount(),
            request.currency(),
            "CRYPTO",
            "Transaction broadcasted to blockchain ledger",
            Instant.now()
        );
    }
}
