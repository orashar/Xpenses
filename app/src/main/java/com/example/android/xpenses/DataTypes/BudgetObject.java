package com.example.android.xpenses.DataTypes;

public class BudgetObject {

    String amount, account, fund, category, subCategory, startDate, endDate;

    public BudgetObject(String amount, String account, String fund, String category, String subCategory, String startDate, String endDate) {
        this.amount = amount;
        this.account = account;
        this.fund = fund;
        this.category = category;
        this.subCategory = subCategory;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getAccount() {
        return account;
    }

    public String getFund() {
        return fund;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
