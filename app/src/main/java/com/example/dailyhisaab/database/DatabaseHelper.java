/*
 * Copyright (c) 2026 Abhay Sachin Donde. All rights reserved.
 * This file is part of DailyHisaab, which is proprietary software.
 * Unauthorized copying, modification, or distribution is strictly prohibited.
 */
package com.example.dailyhisaab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dailyhisaab.models.Expense;
import com.example.dailyhisaab.utils.AppConstants;
import com.example.dailyhisaab.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_CREATE =
            "CREATE TABLE " + AppConstants.TABLE_EXPENSES + " (" +
                    AppConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AppConstants.COLUMN_TITLE + " TEXT NOT NULL, " +
                    AppConstants.COLUMN_AMOUNT + " REAL NOT NULL, " +
                    AppConstants.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                    AppConstants.COLUMN_DATE + " TEXT NOT NULL, " +
                    AppConstants.COLUMN_NOTE + " TEXT, " +
                    AppConstants.COLUMN_PAYMENT_METHOD + " TEXT DEFAULT 'Cash'" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + AppConstants.TABLE_EXPENSES + " ADD COLUMN " + AppConstants.COLUMN_PAYMENT_METHOD + " TEXT DEFAULT 'Cash'");
        }
    }

    public long insertExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.COLUMN_TITLE, expense.getTitle());
        values.put(AppConstants.COLUMN_AMOUNT, expense.getAmount());
        values.put(AppConstants.COLUMN_CATEGORY, expense.getCategory());
        values.put(AppConstants.COLUMN_DATE, expense.getDate());
        values.put(AppConstants.COLUMN_NOTE, expense.getNote());
        values.put(AppConstants.COLUMN_PAYMENT_METHOD, expense.getPaymentMethod());

        long id = db.insert(AppConstants.TABLE_EXPENSES, null, values);
        db.close();
        return id;
    }

    public int updateExpense(long id, Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.COLUMN_TITLE, expense.getTitle());
        values.put(AppConstants.COLUMN_AMOUNT, expense.getAmount());
        values.put(AppConstants.COLUMN_CATEGORY, expense.getCategory());
        values.put(AppConstants.COLUMN_DATE, expense.getDate());
        values.put(AppConstants.COLUMN_NOTE, expense.getNote());
        values.put(AppConstants.COLUMN_PAYMENT_METHOD, expense.getPaymentMethod());

        int rows = db.update(AppConstants.TABLE_EXPENSES, values, AppConstants.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public int deleteExpense(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(AppConstants.TABLE_EXPENSES, AppConstants.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AppConstants.TABLE_EXPENSES + " ORDER BY " + AppConstants.COLUMN_DATE + " DESC, " + AppConstants.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = getExpenseFromCursor(cursor);
                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    public List<Expense> getFilteredExpenses(String query, String categoryFilter) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            selection.append("(").append(AppConstants.COLUMN_TITLE).append(" LIKE ? OR ")
                     .append(AppConstants.COLUMN_NOTE).append(" LIKE ?)");
            selectionArgs.add("%" + query + "%");
            selectionArgs.add("%" + query + "%");
        }

        if (categoryFilter != null && !categoryFilter.equals("All Categories") && !categoryFilter.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(AppConstants.COLUMN_CATEGORY).append(" = ?");
            selectionArgs.add(categoryFilter);
        }

        String selectionStr = selection.length() > 0 ? selection.toString() : null;
        String[] args = selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[0]);

        Cursor cursor = db.query(AppConstants.TABLE_EXPENSES, null, selectionStr, args, null, null, AppConstants.COLUMN_DATE + " DESC, " + AppConstants.COLUMN_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                expenses.add(getExpenseFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    public List<Expense> getRecentExpenses(int limit) {
        List<Expense> expenses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AppConstants.TABLE_EXPENSES + " ORDER BY " + AppConstants.COLUMN_DATE + " DESC, " + AppConstants.COLUMN_ID + " DESC LIMIT " + limit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = getExpenseFromCursor(cursor);
                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    public Expense getExpenseById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(AppConstants.TABLE_EXPENSES, null, AppConstants.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        
        Expense expense = null;
        if (cursor != null && cursor.moveToFirst()) {
            expense = getExpenseFromCursor(cursor);
            cursor.close();
        }
        db.close();
        return expense;
    }

    private Expense getExpenseFromCursor(Cursor cursor) {
        Expense expense = new Expense();
        expense.setId(cursor.getLong(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_ID)));
        expense.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_TITLE)));
        expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_AMOUNT)));
        expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_CATEGORY)));
        expense.setDate(cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_DATE)));
        expense.setNote(cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_NOTE)));
        
        int pmIndex = cursor.getColumnIndex(AppConstants.COLUMN_PAYMENT_METHOD);
        if (pmIndex != -1) {
            expense.setPaymentMethod(cursor.getString(pmIndex));
        } else {
            expense.setPaymentMethod("Cash");
        }
        
        return expense;
    }

    public double getTotalExpense() {
        return getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES);
    }

    public double getTodayExpense() {
        String today = DateUtils.getTodayDate();
        return getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_DATE + " = '" + today + "'");
    }

    public double getMonthlyExpense() {
        return getMonthlyExpense(DateUtils.getCurrentMonthPrefix());
    }

    public double getMonthlyExpense(String monthPrefix) {
        return getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_DATE + " LIKE '" + monthPrefix + "%'");
    }

    public int getExpenseCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + AppConstants.TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public double getAverageExpense() {
        double avg = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) {
            avg = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return avg;
    }

    public double getHighestExpense() {
        double max = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) {
            max = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return max;
    }

    public Map<String, Double> getCategoryWiseTotals() {
        return getCategoryWiseTotals(null);
    }

    public Map<String, Double> getCategoryWiseTotals(String monthPrefix) {
        Map<String, Double> totals = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT " + AppConstants.COLUMN_CATEGORY + ", SUM(" + AppConstants.COLUMN_AMOUNT + ") " +
                       "FROM " + AppConstants.TABLE_EXPENSES;
        if (monthPrefix != null && !monthPrefix.isEmpty()) {
            query += " WHERE " + AppConstants.COLUMN_DATE + " LIKE '" + monthPrefix + "%'";
        }
        query += " GROUP BY " + AppConstants.COLUMN_CATEGORY;
        
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                double total = cursor.getDouble(1);
                totals.put(category, total);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return totals;
    }

    private double getSumAggregate(String query) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }
}