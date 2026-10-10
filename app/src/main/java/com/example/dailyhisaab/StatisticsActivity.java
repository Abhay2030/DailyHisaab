package com.example.dailyhisaab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.dailyhisaab.database.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvStatTotal, tvStatCount, tvStatAverage, tvStatHighest;
    private LinearLayout layoutCategoryStats;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupToolbar();
        setupListeners();
    }

    private void initViews() {
        tvStatTotal = findViewById(R.id.tvStatTotal);
        tvStatCount = findViewById(R.id.tvStatCount);
        tvStatAverage = findViewById(R.id.tvStatAverage);
        tvStatHighest = findViewById(R.id.tvStatHighest);
        layoutCategoryStats = findViewById(R.id.layoutCategoryStats);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_statistics) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                finish();
                return false;
            } else if (itemId == R.id.navigation_history) {
                startActivity(new Intent(StatisticsActivity.this, HistoryActivity.class));
                finish();
                return false;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatisticsData();
        bottomNavigation.getMenu().findItem(R.id.navigation_statistics).setChecked(true);
    }

    private void loadStatisticsData() {
        double total = dbHelper.getTotalExpense();
        int count = dbHelper.getExpenseCount();
        double avg = dbHelper.getAverageExpense();
        double highest = dbHelper.getHighestExpense();

        tvStatTotal.setText(String.format(Locale.getDefault(), "₹%.2f", total));
        tvStatCount.setText(String.valueOf(count));
        tvStatAverage.setText(String.format(Locale.getDefault(), "₹%.2f", avg));
        tvStatHighest.setText(String.format(Locale.getDefault(), "₹%.2f", highest));

        layoutCategoryStats.removeAllViews();
        Map<String, Double> categoryTotals = dbHelper.getCategoryWiseTotals();
        
        if (categoryTotals.isEmpty()) {
            TextView tvNoData = new TextView(this);
            tvNoData.setText(getString(R.string.no_statistics));
            tvNoData.setPadding(16, 16, 16, 16);
            layoutCategoryStats.addView(tvNoData);
            return;
        }

        // Find max for scaling
        double maxTotal = 0;
        for (Double val : categoryTotals.values()) {
            if (val > maxTotal) maxTotal = val;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            View view = inflater.inflate(R.layout.item_category_stat, layoutCategoryStats, false);
            TextView tvCategory = view.findViewById(R.id.tvStatCategoryName);
            TextView tvTotal = view.findViewById(R.id.tvStatCategoryTotal);
            LinearProgressIndicator progressCategory = view.findViewById(R.id.progressCategory);
            
            tvCategory.setText(entry.getKey());
            tvTotal.setText(String.format(Locale.getDefault(), "₹%.2f", entry.getValue()));
            
            int progress = 0;
            if (maxTotal > 0) {
                progress = (int) ((entry.getValue() / maxTotal) * 100);
            }
            progressCategory.setProgress(progress);
            
            layoutCategoryStats.addView(view);
        }
    }
}