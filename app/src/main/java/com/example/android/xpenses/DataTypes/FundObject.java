package com.example.android.xpenses.DataTypes;

public class FundObject {
    private String fund, account, initialBal, expense, budget;

    public FundObject(String fund, String account, String initialBal, String expense, String budget) {
        this.fund = fund;
        this.account = account;
        this.initialBal = initialBal;
        this.expense = expense;
        this.budget = budget;
    }

    public String getFund() {
        return fund;
    }

    public String getAccount() {
        return account;
    }

    public String getInitialBal() {
        return initialBal;
    }

    public String getExpense() {
        return expense;
    }

    public String getBudget() {
        return budget;
    }
}
