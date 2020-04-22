package com.example.android.xpenses.DataTypes;

public class TitleAmountObject {
    String title, amount;

    public TitleAmountObject(String title, String amount) {
        this.title = title;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }
}
