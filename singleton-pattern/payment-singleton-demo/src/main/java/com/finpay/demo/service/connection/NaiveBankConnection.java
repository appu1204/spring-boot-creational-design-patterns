package com.finpay.demo.service.connection;

import java.util.concurrent.atomic.AtomicInteger;

public class NaiveBankConnection {
    
    public static final AtomicInteger instanceCounter = new AtomicInteger();

    private final String connectionId;

    public NaiveBankConnection() {
        int count = instanceCounter.incrementAndGet();
        this.connectionId = "CONN-NAIVE-#" + count + "-@" + Integer.toHexString(this.hashCode());
        
        try{
            System.out.println("Creating new Connection.. " + connectionId);
            Thread.sleep(2000);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String pay(double amount) {
        return "Paid $" + amount + " successfully using " + connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

}
