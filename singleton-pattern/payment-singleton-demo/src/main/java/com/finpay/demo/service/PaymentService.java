package com.finpay.demo.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.finpay.demo.dto.response.PaymentResponse;
import com.finpay.demo.service.connection.BillPughBankConnection;
import com.finpay.demo.service.connection.DclBankConnection;
import com.finpay.demo.service.connection.NaiveBankConnection;

@Service 
public class PaymentService {

    public PaymentResponse processWithoutSingleton(double amount) {
        long start = System.currentTimeMillis();
        NaiveBankConnection connection = new NaiveBankConnection();
        connection.pay(amount);

        long duration = System.currentTimeMillis() - start;
        return new PaymentResponse(
                "SUCCESS",
                connection.getConnectionId(),
                duration,
                NaiveBankConnection.instanceCounter.get(),
                "Slow! Every request connect the bank from scratch."
        );
    }

    public PaymentResponse processWithDcl(double amount) {
        long start = System.currentTimeMillis();
        DclBankConnection connection = DclBankConnection.getInstance();

        connection.pay(amount);

        long duration = System.currentTimeMillis() - start;
        return new PaymentResponse(
                "SUCCESS",
                connection.getConnectionId(),
                duration,
                DclBankConnection.instanceCounter.get(),
                duration > 1500 ? "First request initialized the Singleton." : "Fast! Reused open connection."
        );
    }

    public PaymentResponse processWithBillPugh(double amount) {
        long start = System.currentTimeMillis();
        BillPughBankConnection connection = BillPughBankConnection.getInstance();

        connection.pay(amount);

        long duration = System.currentTimeMillis() - start;
        return new PaymentResponse(
                "SUCCESS",
                connection.getConnectionId(),
                duration,
                BillPughBankConnection.instanceCounter.get(),
                duration > 1500 ? "First request initialized the Singleton." : "Fast! Reused open connection."
        );
    }

    public Map<String, Object> simulateConcurrentLoad(String mode, int count) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(count);
        CountDownLatch latch = new CountDownLatch(count);
        Set<String> uniqueInstances = ConcurrentHashMap.newKeySet();
    
        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    if ("naive".equalsIgnoreCase(mode)) {
                        NaiveBankConnection conn = new NaiveBankConnection();
                        uniqueInstances.add(conn.getConnectionId());
                    } else if ("billpugh".equalsIgnoreCase(mode)) {
                        BillPughBankConnection conn = BillPughBankConnection.getInstance();
                        uniqueInstances.add(conn.getConnectionId());
                    } else {
                        DclBankConnection conn = DclBankConnection.getInstance();
                        uniqueInstances.add(conn.getConnectionId());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
    
        latch.await();
        executor.shutdown();
        long totalTime = System.currentTimeMillis() - start;
    
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("modeTested", mode);
        result.put("totalRequestsSent", count);
        result.put("uniqueObjectsCreated", uniqueInstances.size());
        result.put("totalTimeTakenMs", totalTime);
    
        result.put("instancesList", uniqueInstances);
    
        result.put("summary", uniqueInstances.size() == 1 
                ? "EXCELLENT: All requests shared exactly 1 instance!" 
                : "POOR: Created " + uniqueInstances.size() + " duplicate heavy objects!");
    
        return result;
    }
}
