package com.example.dailyhisaab.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy";

    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentMonthPrefix() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getPreviousMonthPrefix() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    public static String formatForDisplay(String dbDate) {
        try {
            SimpleDateFormat dbSdf = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
            Date date = dbSdf.parse(dbDate);
            SimpleDateFormat displaySdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
            return displaySdf.format(date);
        } catch (Exception e) {
            return dbDate; // return original on error
        }
    }
}