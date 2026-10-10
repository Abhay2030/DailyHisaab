package com.example.dailyhisaab.models;

import java.io.Serializable;

public class Expense implements Serializable {
    private long id;
    private String title;
    private double amount;
    private String category;
    private String date; // YYYY-MM-DD
    private String note;
    private String paymentMethod;

    public Expense() {
    }

    public Expense(long id, String title, double amount, String category, String date, String note, String paymentMethod) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
        this.paymentMethod = paymentMethod;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}