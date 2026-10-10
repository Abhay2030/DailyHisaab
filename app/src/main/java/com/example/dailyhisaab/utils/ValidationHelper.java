package com.example.dailyhisaab.utils;

import android.text.TextUtils;

public class ValidationHelper {
    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    public static boolean isValidAmount(String amountStr) {
        if (TextUtils.isEmpty(amountStr)) return false;
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0 && amount <= 999999999.99; // Some reasonable limit
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidCategory(String category) {
        return category != null && !category.trim().isEmpty() && !category.equals("Select Category") && AppConstants.CATEGORIES.contains(category);
    }

    public static boolean isValidDate(String date) {
        return date != null && !date.trim().isEmpty();
    }

    public static boolean isValidNote(String note) {
        if (note == null) return true;
        return note.length() <= 200;
    }
}