package com.example.bai3_buoi2;

public class Transaction {
    private String date;
    private String name;
    private String info;
    private String amount;

    public Transaction(String date, String name, String info, String amount) {
        this.date = date;
        this.name = name;
        this.info = info;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getAmount() {
        return amount;
    }
}

