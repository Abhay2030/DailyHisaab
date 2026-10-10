package com.example.dailyhisaab;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.dailyhisaab.utils.AppConstants;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        setupToolbar();
        setupListView();
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

    private void setupListView() {
        ListView lvCategories = findViewById(R.id.lvCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.item_category, R.id.tvCategoryName, AppConstants.CATEGORIES);
        lvCategories.setAdapter(adapter);
    }
}