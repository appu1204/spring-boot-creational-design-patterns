package com.finpay.demo.service.connection;

import java.util.concurrent.atomic.AtomicInteger;

public class BillPughBankConnection {

    public static final AtomicInteger instanceCounter = new AtomicInteger();

    private final String connectionId;

    private BillPughBankConnection() {
        int count = instanceCounter.incrementAndGet();
        this.connectionId = "CONN-BILLPUGH-#" + count + "-@" + Integer.toHexString(this.hashCode());

        try {
            System.out.println("Establishing Bill Pugh Singleton connection: " + connectionId);
            Thread.sleep(2000);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class ConnectionHolder {
        private static final BillPughBankConnection INSTANCE = new BillPughBankConnection();
    }

    public static BillPughBankConnection getInstance() {
        return ConnectionHolder.INSTANCE;
    }

    public String pay(double amount) {
        return "Paid $" + amount + " successfully using " + connectionId;
    }
    public String getConnectionId() {
        return connectionId;
    }

}
