package com.example.dailyhisaab;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.dailyhisaab.database.DatabaseHelper;
import com.example.dailyhisaab.models.Expense;
import com.example.dailyhisaab.utils.AppConstants;
import com.example.dailyhisaab.utils.DateUtils;
import com.example.dailyhisaab.utils.ValidationHelper;
import com.example.dailyhisaab.utils.SettingsManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etAmount, etDate, etNote;
    private Spinner spinnerCategory, spinnerPaymentMethod;
    private MaterialButton btnSave, btnCancel;
    private DatabaseHelper dbHelper;
    private SettingsManager settingsManager;
    private long expenseIdToEdit = -1;
    private String selectedDbDate; // YYYY-MM-DD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DatabaseHelper(this);
        settingsManager = new SettingsManager(this);
        initViews();
        setupToolbar();
        setupCategorySpinner();
        setupPaymentMethodSpinner();
        setupListeners();
        checkIfEditMode();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etNote = findViewById(R.id.etNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
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

    private void setupCategorySpinner() {
        List<String> categoriesWithHint = new ArrayList<>();
        categoriesWithHint.add(getString(R.string.select_category));
        categoriesWithHint.addAll(AppConstants.CATEGORIES);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, categoriesWithHint);
        spinnerCategory.setAdapter(adapter);
    }
    
    private void setupPaymentMethodSpinner() {
        List<String> methods = new ArrayList<>();
        methods.add("Cash");
        methods.add("UPI");
        methods.add("Card");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, methods);
        spinnerPaymentMethod.setAdapter(adapter);
    }

    private void setupListeners() {
        etDate.setOnClickListener(v -> showDatePicker());
        
        btnCancel.setOnClickListener(v -> finish());
        
        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDbDate != null && !selectedDbDate.isEmpty()) {
            try {
                String[] parts = selectedDbDate.split("-");
                calendar.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
            } catch (Exception ignored) {}
        }

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDbDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            etDate.setText(DateUtils.formatForDisplay(selectedDbDate));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void checkIfEditMode() {
        if (getIntent().hasExtra("expense_id")) {
            expenseIdToEdit = getIntent().getLongExtra("expense_id", -1);
            if (expenseIdToEdit != -1) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.edit_expense_title));
                }
                loadExpenseDetails(expenseIdToEdit);
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.add_expense_title));
            }
            // default date to today
            selectedDbDate = DateUtils.getTodayDate();
            etDate.setText(DateUtils.formatForDisplay(selectedDbDate));
            
            // Preselect last used category
            String lastCat = settingsManager.getLastCategory();
            if (lastCat != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).equals(lastCat)) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
            
            // Auto focus amount to speed up entry
            etAmount.requestFocus();
        }
    }

    private void loadExpenseDetails(long id) {
        Expense expense = dbHelper.getExpenseById(id);
        if (expense != null) {
            etTitle.setText(expense.getTitle());
            etAmount.setText(String.valueOf(expense.getAmount()));
            selectedDbDate = expense.getDate();
            etDate.setText(DateUtils.formatForDisplay(selectedDbDate));
            etNote.setText(expense.getNote());
            
            // set category
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i) != null && adapter.getItem(i).equals(expense.getCategory())) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
            
            // set payment method
            ArrayAdapter<String> pmAdapter = (ArrayAdapter<String>) spinnerPaymentMethod.getAdapter();
            for (int i = 0; i < pmAdapter.getCount(); i++) {
                if (pmAdapter.getItem(i) != null && pmAdapter.getItem(i).equals(expense.getPaymentMethod())) {
                    spinnerPaymentMethod.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveExpense() {
        String title = etTitle.getText() != null ? etTitle.getText().toString() : "";
        String amountStr = etAmount.getText() != null ? etAmount.getText().toString() : "";
        String category = spinnerCategory.getSelectedItem().toString();
        String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
        String note = etNote.getText() != null ? etNote.getText().toString() : "";

        if (!ValidationHelper.isValidTitle(title)) {
            etTitle.setError(getString(R.string.error_empty_title));
            return;
        }

        if (!ValidationHelper.isValidAmount(amountStr)) {
            etAmount.setError(getString(R.string.error_invalid_amount));
            return;
        }

        if (!ValidationHelper.isValidCategory(category)) {
            Toast.makeText(this, getString(R.string.error_empty_category), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationHelper.isValidDate(selectedDbDate)) {
            etDate.setError(getString(R.string.error_empty_date));
            return;
        }
        
        if (!ValidationHelper.isValidNote(note)) {
            etNote.setError(getString(R.string.error_note_too_long));
            return;
        }

        // Disable save button to prevent double tap
        btnSave.setEnabled(false);

        double amount = Double.parseDouble(amountStr);
        Expense expense = new Expense(-1, title.trim(), amount, category, selectedDbDate, note.trim(), paymentMethod);

        long result;
        if (expenseIdToEdit != -1) {
            result = dbHelper.updateExpense(expenseIdToEdit, expense);
        } else {
            result = dbHelper.insertExpense(expense);
        }

        if (result != -1 && result != 0) {
            settingsManager.setLastCategory(category);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.error_saving), Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        }
    }
}