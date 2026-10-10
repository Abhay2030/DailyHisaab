package com.example.dailyhisaab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.dailyhisaab.database.DatabaseHelper;
import com.example.dailyhisaab.models.Expense;
import com.example.dailyhisaab.utils.SettingsManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;
    private DatabaseHelper dbHelper;
    private TextInputEditText etMonthlyBudget;

    private final ActivityResultLauncher<Intent> exportCsvLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        exportDataToUri(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);
        dbHelper = new DatabaseHelper(this);

        setupToolbar();
        initViews();
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

    private void initViews() {
        etMonthlyBudget = findViewById(R.id.etMonthlyBudget);
        float budget = settingsManager.getMonthlyBudget();
        if (budget > 0) {
            etMonthlyBudget.setText(String.valueOf(budget));
        }
    }

    private void setupListeners() {
        MaterialButton btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnSaveBudget.setOnClickListener(v -> {
            String val = etMonthlyBudget.getText() != null ? etMonthlyBudget.getText().toString() : "";
            try {
                float budget = Float.parseFloat(val);
                settingsManager.setMonthlyBudget(budget);
                Toast.makeText(this, "Budget saved", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                if (val.isEmpty()) {
                    settingsManager.setMonthlyBudget(0);
                    Toast.makeText(this, "Budget cleared", Toast.LENGTH_SHORT).show();
                } else {
                    etMonthlyBudget.setError(getString(R.string.error_invalid_amount));
                }
            }
        });

        findViewById(R.id.tvExport).setOnClickListener(v -> startExportFlow());

        findViewById(R.id.tvClearData).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.clear_all_data)
                    .setMessage(R.string.clear_data_confirm)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        // Recreate database essentially
                        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);
                        Toast.makeText(this, "Data cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        findViewById(R.id.tvAboutLegal).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AboutLegalActivity.class));
        });
    }

    private void startExportFlow() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "dailyhisaab_export.csv");
        exportCsvLauncher.launch(intent);
    }

    private void exportDataToUri(Uri uri) {
        try (OutputStream outputStream = getContentResolver().openOutputStream(uri);
             PrintWriter writer = new PrintWriter(outputStream)) {
             
            List<Expense> expenses = dbHelper.getAllExpenses();
            
            // Header
            writer.println("ID,Title,Amount,Category,Date,Note");
            
            // Data
            for (Expense exp : expenses) {
                // Escape commas by quoting string fields
                String title = escapeCsv(exp.getTitle());
                String category = escapeCsv(exp.getCategory());
                String note = escapeCsv(exp.getNote());
                writer.println(String.format("%d,%s,%f,%s,%s,%s",
                        exp.getId(), title, exp.getAmount(), category, exp.getDate(), note));
            }
            writer.flush();
            Toast.makeText(this, getString(R.string.export_success), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}