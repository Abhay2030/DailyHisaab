package com.example.dailyhisaab.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREFS_NAME = "dailyhisaab_prefs";
    private static final String KEY_MONTHLY_BUDGET = "monthly_budget";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public float getMonthlyBudget() {
        return prefs.getFloat(KEY_MONTHLY_BUDGET, 0f);
    }

    public void setMonthlyBudget(float budget) {
        prefs.edit().putFloat(KEY_MONTHLY_BUDGET, budget).apply();
    }
    
    public void clearAllSettings() {
        prefs.edit().clear().apply();
    }
}