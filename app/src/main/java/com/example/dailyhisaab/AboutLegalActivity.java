/*
 * Copyright (c) 2026 Abhay Sachin Donde. All rights reserved.
 * This file is part of DailyHisaab, which is proprietary software.
 * Unauthorized copying, modification, or distribution is strictly prohibited.
 */
package com.example.dailyhisaab;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutLegalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_legal);

        setupToolbar();
        setupListeners();
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
        findViewById(R.id.tvPrivacyPolicy).setOnClickListener(v -> 
            showLegalDialog(getString(R.string.privacy_policy), getString(R.string.privacy_policy_text))
        );

        findViewById(R.id.tvTerms).setOnClickListener(v -> 
            showLegalDialog(getString(R.string.terms_conditions), getString(R.string.terms_conditions_text))
        );

        findViewById(R.id.tvLicenses).setOnClickListener(v -> 
            showLegalDialog(getString(R.string.open_source_licenses), getString(R.string.open_source_licenses_text))
        );
    }

    private void showLegalDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}