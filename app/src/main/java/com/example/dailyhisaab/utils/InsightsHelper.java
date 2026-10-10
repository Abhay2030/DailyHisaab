package com.example.dailyhisaab.utils;

import android.content.Context;

import com.example.dailyhisaab.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InsightsHelper {

    public static List<String> generateInsights(Context context, DatabaseHelper dbHelper) {
        List<String> insights = new ArrayList<>();
        
        String currentMonth = DateUtils.getCurrentMonthPrefix();
        String previousMonth = DateUtils.getPreviousMonthPrefix();
        
        double currentMonthTotal = dbHelper.getMonthlyExpense(currentMonth);
        double prevMonthTotal = dbHelper.getMonthlyExpense(previousMonth);
        
        // 1. Budget Insight
        SettingsManager settings = new SettingsManager(context);
        float budget = settings.getMonthlyBudget();
        if (budget > 0) {
            double utilization = (currentMonthTotal / budget) * 100;
            if (utilization >= 100) {
                insights.add(String.format(Locale.getDefault(), "Warning: You have exceeded your monthly budget by %s.", FormatUtils.formatRupee(currentMonthTotal - budget)));
            } else if (utilization >= 80) {
                insights.add(String.format(Locale.getDefault(), "Caution: You have used %.0f%% of your monthly budget.", utilization));
            } else {
                insights.add("Great job! You are well within your monthly budget.");
            }
        } else {
            insights.add("Set a monthly budget in Settings to track your spending limits.");
        }

        // 2. Top Spending Category
        Map<String, Double> categoryTotals = dbHelper.getCategoryWiseTotals(currentMonth);
        if (!categoryTotals.isEmpty()) {
            String topCategory = "";
            double maxCatAmount = 0;
            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                if (entry.getValue() > maxCatAmount) {
                    maxCatAmount = entry.getValue();
                    topCategory = entry.getKey();
                }
            }
            if (maxCatAmount > 0) {
                insights.add(String.format(Locale.getDefault(), "%s is your highest spending category this month (%s).", topCategory, FormatUtils.formatRupee(maxCatAmount)));
            }
        }

        // 3. Month over Month Comparison
        if (prevMonthTotal > 0 && currentMonthTotal > 0) {
            if (currentMonthTotal > prevMonthTotal) {
                double pctIncrease = ((currentMonthTotal - prevMonthTotal) / prevMonthTotal) * 100;
                insights.add(String.format(Locale.getDefault(), "You have spent %.0f%% more this month compared to last month.", pctIncrease));
            } else if (currentMonthTotal < prevMonthTotal) {
                double pctDecrease = ((prevMonthTotal - currentMonthTotal) / prevMonthTotal) * 100;
                insights.add(String.format(Locale.getDefault(), "Good progress! You have spent %.0f%% less than last month.", pctDecrease));
            }
        } else if (currentMonthTotal > 0 && prevMonthTotal == 0) {
            insights.add("This is your first month tracking expenses. Keep it up!");
        }

        if (insights.isEmpty()) {
            insights.add("Record some expenses to see smart insights here.");
        }

        return insights;
    }
}