package com.example.dailyhisaab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailyhisaab.adapters.ExpenseAdapter;
import com.example.dailyhisaab.database.DatabaseHelper;
import com.example.dailyhisaab.models.Expense;
import com.example.dailyhisaab.utils.AppConstants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {

    private DatabaseHelper dbHelper;
    private RecyclerView rvHistory;
    private TextView tvEmptyHistory;
    private ExpenseAdapter adapter;
    private BottomNavigationView bottomNavigation;
    private SearchView searchView;
    private Spinner spinnerFilterCategory;
    
    private String currentQuery = "";
    private String currentCategory = "All Categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupFilters();
        setupListeners();
    }

    private void initViews() {
        rvHistory = findViewById(R.id.rvHistory);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        searchView = findViewById(R.id.searchView);
        spinnerFilterCategory = findViewById(R.id.spinnerFilterCategory);
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
    
    private void setupFilters() {
        List<String> filterCategories = new ArrayList<>();
        filterCategories.add(getString(R.string.filter_all));
        filterCategories.addAll(AppConstants.CATEGORIES);
        
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, filterCategories);
        spinnerFilterCategory.setAdapter(catAdapter);
        
        spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = filterCategories.get(position);
                loadHistoryData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                loadHistoryData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText;
                loadHistoryData();
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(this, null, true, this);
        rvHistory.setAdapter(adapter);
    }

    private void setupListeners() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_history) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                finish();
                return false;
            } else if (itemId == R.id.navigation_statistics) {
                startActivity(new Intent(HistoryActivity.this, StatisticsActivity.class));
                finish();
                return false;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistoryData();
        bottomNavigation.getMenu().findItem(R.id.navigation_history).setChecked(true);
    }

    private void loadHistoryData() {
        List<Expense> expenses;
        if (currentQuery.isEmpty() && currentCategory.equals(getString(R.string.filter_all))) {
            expenses = dbHelper.getAllExpenses();
        } else {
            expenses = dbHelper.getFilteredExpenses(currentQuery, currentCategory);
        }
        
        if (expenses.isEmpty()) {
            rvHistory.setVisibility(View.GONE);
            tvEmptyHistory.setVisibility(View.VISIBLE);
        } else {
            rvHistory.setVisibility(View.VISIBLE);
            tvEmptyHistory.setVisibility(View.GONE);
            adapter.setExpenses(expenses);
        }
    }

    @Override
    public void onEditClick(Expense expense) {
        Intent intent = new Intent(this, AddExpenseActivity.class);
        intent.putExtra("expense_id", expense.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Expense expense, int position) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_confirmation_title))
                .setMessage(getString(R.string.delete_confirmation_message))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    dbHelper.deleteExpense(expense.getId());
                    loadHistoryData();
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}