package com.example.dailyhisaab.utils

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    
    // Indian Locale for formatting (₹1,25,000.00)
    private val INDIA_LOCALE = Locale("en", "IN")
    
    @JvmStatic
    fun formatRupee(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(INDIA_LOCALE)
        return formatter.format(amount)
    }
}
