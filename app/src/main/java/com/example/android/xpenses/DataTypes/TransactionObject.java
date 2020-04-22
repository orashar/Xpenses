package com.example.android.xpenses.DataTypes;

public class TransactionObject {
    String transactionTitle, transactionAmount, transactionTag, transactionDate, transactionType;

    public TransactionObject(String transactionTitle, String transactionAmount, String transactionTag, String transactionDate, String transactionType) {
        this.transactionTitle = transactionTitle;
        this.transactionAmount = transactionAmount;
        this.transactionTag = transactionTag;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    public String getTransactionTitle() {
        return transactionTitle;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionTag() {
        return transactionTag;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
