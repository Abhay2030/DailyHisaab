package com.example.dailyhisaab.utils

import android.content.Context
import com.example.dailyhisaab.database.DatabaseHelper
import java.util.Locale

object InsightsHelper {

    @JvmStatic
    fun generateInsights(context: Context, dbHelper: DatabaseHelper): List<String> {
        val insights = mutableListOf<String>()

        val currentMonth = DateUtils.getCurrentMonthPrefix()
        val previousMonth = DateUtils.getPreviousMonthPrefix()

        val currentMonthTotal = dbHelper.getMonthlyExpense(currentMonth)
        val prevMonthTotal = dbHelper.getMonthlyExpense(previousMonth)

        // 1. Budget Insight
        val settings = SettingsManager(context)
        val budget = settings.monthlyBudget
        if (budget > 0) {
            val utilization = (currentMonthTotal / budget) * 100
            if (utilization >= 100) {
                insights.add(
                    String.format(
                        Locale.getDefault(),
                        "Warning: You have exceeded your monthly budget by %s.",
                        FormatUtils.formatRupee(currentMonthTotal - budget)
                    )
                )
            } else if (utilization >= 80) {
                insights.add(
                    String.format(
                        Locale.getDefault(),
                        "Caution: You have used %.0f%% of your monthly budget.",
                        utilization
                    )
                )
            } else {
                insights.add("Great job! You are well within your monthly budget.")
            }
        } else {
            insights.add("Set a monthly budget in Settings to track your spending limits.")
        }

        // 2. Top Spending Category
        val categoryTotals = dbHelper.getCategoryWiseTotals(currentMonth)
        if (categoryTotals.isNotEmpty()) {
            var topCategory = ""
            var maxCatAmount = 0.0
            for ((key, value) in categoryTotals) {
                if (value > maxCatAmount) {
                    maxCatAmount = value
                    topCategory = key
                }
            }
            if (maxCatAmount > 0) {
                insights.add(
                    String.format(
                        Locale.getDefault(),
                        "%s is your highest spending category this month (%s).",
                        topCategory,
                        FormatUtils.formatRupee(maxCatAmount)
                    )
                )
            }
        }

        // 3. Month over Month Comparison
        if (prevMonthTotal > 0 && currentMonthTotal > 0) {
            if (currentMonthTotal > prevMonthTotal) {
                val pctIncrease = ((currentMonthTotal - prevMonthTotal) / prevMonthTotal) * 100
                insights.add(
                    String.format(
                        Locale.getDefault(),
                        "You have spent %.0f%% more this month compared to last month.",
                        pctIncrease
                    )
                )
            } else if (currentMonthTotal < prevMonthTotal) {
                val pctDecrease = ((prevMonthTotal - currentMonthTotal) / prevMonthTotal) * 100
                insights.add(
                    String.format(
                        Locale.getDefault(),
                        "Good progress! You have spent %.0f%% less than last month.",
                        pctDecrease
                    )
                )
            }
        } else if (currentMonthTotal > 0 && prevMonthTotal == 0.0) {
            insights.add("This is your first month tracking expenses. Keep it up!")
        }

        if (insights.isEmpty()) {
            insights.add("Record some expenses to see smart insights here.")
        }

        return insights
    }
}
