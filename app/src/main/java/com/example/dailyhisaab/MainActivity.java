/*
 * Copyright (c) 2026 Abhay Sachin Donde. All rights reserved.
 * This file is part of DailyHisaab, which is proprietary software.
 * Unauthorized copying, modification, or distribution is strictly prohibited.
 */
package com.example.dailyhisaab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailyhisaab.adapters.ExpenseAdapter;
import com.example.dailyhisaab.database.DatabaseHelper;
import com.example.dailyhisaab.models.Expense;
import com.example.dailyhisaab.utils.InsightsHelper;
import com.example.dailyhisaab.utils.SettingsManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SettingsManager settingsManager;
    private TextView tvTotalExpenses, tvTodayExpenses, tvMonthExpenses, tvEmptyRecent, tvViewAll, tvBudgetRemaining;
    private LinearLayout layoutBudget, layoutInsights;
    private LinearProgressIndicator progressBudget;
    private RecyclerView rvRecentExpenses;
    private ExpenseAdapter adapter;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        settingsManager = new SettingsManager(this);
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        tvTodayExpenses = findViewById(R.id.tvTodayExpenses);
        tvMonthExpenses = findViewById(R.id.tvMonthExpenses);
        tvEmptyRecent = findViewById(R.id.tvEmptyRecent);
        tvViewAll = findViewById(R.id.tvViewAll);
        tvBudgetRemaining = findViewById(R.id.tvBudgetRemaining);
        layoutBudget = findViewById(R.id.layoutBudget);
        layoutInsights = findViewById(R.id.layoutInsights);
        progressBudget = findViewById(R.id.progressBudget);
        rvRecentExpenses = findViewById(R.id.rvRecentExpenses);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabAddExpense = findViewById(R.id.fabAddExpense);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        rvRecentExpenses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(this, null, true, new ExpenseAdapter.OnExpenseClickListener() {
            @Override
            public void onEditClick(Expense expense) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                intent.putExtra("expense_id", expense.getId());
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(Expense expense, int position) {
                // To keep it simple in dashboard, open history or delete directly
                dbHelper.deleteExpense(expense.getId());
                loadDashboardData();
            }
        });
        rvRecentExpenses.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAddExpense.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddExpenseActivity.class));
        });

        tvViewAll.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_history) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                return false;
            } else if (itemId == R.id.navigation_statistics) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                return false;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
        bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    private void loadDashboardData() {
        double total = dbHelper.getTotalExpense();
        double today = dbHelper.getTodayExpense();
        double month = dbHelper.getMonthlyExpense();

        tvTotalExpenses.setText(String.format(Locale.getDefault(), "₹%.2f", total));
        tvTodayExpenses.setText(String.format(Locale.getDefault(), "₹%.2f", today));
        tvMonthExpenses.setText(String.format(Locale.getDefault(), "₹%.2f", month));

        float monthlyBudget = settingsManager.getMonthlyBudget();
        if (monthlyBudget > 0) {
            layoutBudget.setVisibility(View.VISIBLE);
            double remaining = monthlyBudget - month;
            if (remaining < 0) remaining = 0;
            
            tvBudgetRemaining.setText(String.format(Locale.getDefault(), getString(R.string.remaining_budget), "₹" + String.format(Locale.getDefault(), "%.2f", remaining)));
            int progress = (int) ((month / monthlyBudget) * 100);
            progressBudget.setProgress(Math.min(progress, 100));
            
            if (progress >= 100) {
                progressBudget.setIndicatorColor(getResources().getColor(R.color.error, getTheme()));
            } else if (progress >= 80) {
                progressBudget.setIndicatorColor(getResources().getColor(R.color.warning, getTheme()));
            } else {
                progressBudget.setIndicatorColor(getResources().getColor(R.color.primary, getTheme()));
            }
        } else {
            layoutBudget.setVisibility(View.GONE);
        }

        populateInsights();

        List<Expense> recentExpenses = dbHelper.getRecentExpenses(3);
        if (recentExpenses.isEmpty()) {
            rvRecentExpenses.setVisibility(View.GONE);
            tvEmptyRecent.setVisibility(View.VISIBLE);
        } else {
            rvRecentExpenses.setVisibility(View.VISIBLE);
            tvEmptyRecent.setVisibility(View.GONE);
            adapter.setExpenses(recentExpenses);
        }
    }

    private void populateInsights() {
        layoutInsights.removeAllViews();
        List<String> insights = InsightsHelper.generateInsights(this, dbHelper);
        
        for (int i = 0; i < insights.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText("• " + insights.get(i));
            tv.setTextColor(getResources().getColor(R.color.textSecondary, getTheme()));
            tv.setPadding(0, 4, 0, 4);
            tv.setTextSize(14f);
            layoutInsights.addView(tv);
        }
    }
}