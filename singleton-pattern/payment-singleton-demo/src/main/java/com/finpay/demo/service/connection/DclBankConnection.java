package com.finpay.demo.service.connection;

import java.util.concurrent.atomic.AtomicInteger;

public class DclBankConnection {

    public static final AtomicInteger instanceCounter = new AtomicInteger();

    private static volatile DclBankConnection instance;

    private final String connectionId;

    private DclBankConnection() {
        int count = instanceCounter.incrementAndGet();
        this.connectionId = "CONN-DCL-#" + count + "-@" + Integer.toHexString(this.hashCode());

        try {
            System.out.println("Establishing Singleton DCL connection: " + connectionId);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static DclBankConnection getInstance() {
        if (instance == null) {
            
            synchronized(DclBankConnection.class) {
                if (instance==null) {
                    instance = new DclBankConnection();
                }
            }
        }   
        return instance;
    }

    public String pay(double amount) {
        return "Paid $" + amount + " successfully using " + connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

}
