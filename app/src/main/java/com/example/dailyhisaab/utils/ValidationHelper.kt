package com.example.dailyhisaab.utils

import android.text.TextUtils

object ValidationHelper {
    
    @JvmStatic
    fun isValidTitle(title: String?): Boolean {
        return !title.isNullOrBlank()
    }

    @JvmStatic
    fun isValidAmount(amountStr: String?): Boolean {
        if (amountStr.isNullOrEmpty()) return false
        return try {
            val amount = amountStr.toDouble()
            amount > 0 && amount <= 999999999.99 // Some reasonable limit
        } catch (e: NumberFormatException) {
            false
        }
    }

    @JvmStatic
    fun isValidCategory(category: String?): Boolean {
        return !category.isNullOrBlank() && category != "Select Category" && AppConstants.CATEGORIES.contains(category)
    }

    @JvmStatic
    fun isValidDate(date: String?): Boolean {
        return !date.isNullOrBlank()
    }

    @JvmStatic
    fun isValidNote(note: String?): Boolean {
        if (note == null) return true
        return note.length <= 200
    }
}
