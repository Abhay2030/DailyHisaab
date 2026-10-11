package com.example.dailyhisaab.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    const val DB_DATE_FORMAT = "yyyy-MM-dd"
    const val DISPLAY_DATE_FORMAT = "dd MMM yyyy"

    @JvmStatic
    fun getTodayDate(): String {
        val sdf = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    @JvmStatic
    fun getCurrentMonthPrefix(): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return sdf.format(Date())
    }

    @JvmStatic
    fun getPreviousMonthPrefix(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return sdf.format(cal.time)
    }

    @JvmStatic
    fun formatForDisplay(dbDate: String?): String {
        if (dbDate == null) return ""
        return try {
            val dbSdf = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
            val date = dbSdf.parse(dbDate)
            val displaySdf = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
            if (date != null) displaySdf.format(date) else dbDate
        } catch (e: Exception) {
            dbDate // return original on error
        }
    }
}
