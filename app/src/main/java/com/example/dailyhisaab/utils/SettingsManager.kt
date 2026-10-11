package com.example.dailyhisaab.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var monthlyBudget: Float
        get() = prefs.getFloat(KEY_MONTHLY_BUDGET, 0f)
        set(budget) {
            prefs.edit().putFloat(KEY_MONTHLY_BUDGET, budget).apply()
        }

    var lastCategory: String?
        get() = prefs.getString(KEY_LAST_CATEGORY, null)
        set(category) {
            prefs.edit().putString(KEY_LAST_CATEGORY, category).apply()
        }

    fun clearAllSettings() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "dailyhisaab_prefs"
        private const val KEY_MONTHLY_BUDGET = "monthly_budget"
        private const val KEY_LAST_CATEGORY = "last_category"
    }
}
