package com.fintech.billing.model;

public class BankRoutingDetails {

    private String bankName;  // -> Bank Name
    private String swiftCode; // -> this is international bank identification code
    private String iban; // -> this international bank account number

    public BankRoutingDetails(String bankName, String swiftCode, String iban) {
        this.bankName = bankName;
        this.swiftCode = swiftCode;
        this.iban = iban;
    }

    /** Deep copy  -> by copying the constructor so that when other request modify this object so
     * that original will be not refelcting the same but what happen is, our goal is to do Deep copy but it still
     * this is Shallow copy -> Reason all the three fields are String and immutable
     * so safe will be to copy the references 
     */

    public BankRoutingDetails(BankRoutingDetails source) {
        this.bankName = source.bankName;
        this.swiftCode = source.swiftCode;
        this.iban = source.iban;
    }

    public String getFormattedRouting() {
        return "Bank: " + bankName + " | SWIFT: " + swiftCode + " | IBAN: " + iban;
    }
}
