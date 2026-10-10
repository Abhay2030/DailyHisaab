package com.example.dailyhisaab.utils;

import java.util.Arrays;
import java.util.List;

public class AppConstants {
    public static final String DATABASE_NAME = "dailyhisaab.db";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_PAYMENT_METHOD = "payment_method";

    // Predefined Categories
    public static final List<String> CATEGORIES = Arrays.asList(
            "Food", "Travel", "Shopping", "Bills", "Education", 
            "Entertainment", "Health", "Fuel", "Other"
    );
}