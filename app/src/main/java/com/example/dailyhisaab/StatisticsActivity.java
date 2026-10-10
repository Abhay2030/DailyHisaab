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
import com.example.dailyhisaab.utils.FormatUtils;
import android.graphics.Color;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvStatTotal, tvStatCount, tvStatAverage, tvStatHighest;
    private LinearLayout layoutCategoryStats;
    private BottomNavigationView bottomNavigation;
    private PieChart pieChart;

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
        pieChart = findViewById(R.id.pieChart);
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

        tvStatTotal.setText(FormatUtils.formatRupee(total));
        tvStatCount.setText(String.valueOf(count));
        tvStatAverage.setText(FormatUtils.formatRupee(avg));
        tvStatHighest.setText(FormatUtils.formatRupee(highest));

        layoutCategoryStats.removeAllViews();
        Map<String, Double> categoryTotals = dbHelper.getCategoryWiseTotals();
        
        if (categoryTotals.isEmpty()) {
            pieChart.setVisibility(View.GONE);
            TextView tvNoData = new TextView(this);
            tvNoData.setText(getString(R.string.no_statistics));
            tvNoData.setPadding(16, 16, 16, 16);
            layoutCategoryStats.addView(tvNoData);
            return;
        } else {
            pieChart.setVisibility(View.VISIBLE);
        }

        // Setup PieChart
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            }
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(16f);
        pieChart.animateY(1000);
        pieChart.invalidate();

        // Find max for scaling bar chart below
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
            tvTotal.setText(FormatUtils.formatRupee(entry.getValue()));
            
            int progress = 0;
            if (maxTotal > 0) {
                progress = (int) ((entry.getValue() / maxTotal) * 100);
            }
            progressCategory.setProgress(progress);
            
            layoutCategoryStats.addView(view);
        }
    }
}