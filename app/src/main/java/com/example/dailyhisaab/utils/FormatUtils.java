package com.example.dailyhisaab.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
    
    // Indian Locale for formatting (₹1,25,000.00)
    private static final Locale INDIA_LOCALE = new Locale("en", "IN");
    
    public static String formatRupee(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(INDIA_LOCALE);
        return formatter.format(amount);
    }
}