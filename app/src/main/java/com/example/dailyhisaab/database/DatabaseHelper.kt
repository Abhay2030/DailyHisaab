/*
 * Copyright (c) 2026 Abhay Sachin Donde. All rights reserved.
 * This file is part of DailyHisaab, which is proprietary software.
 * Unauthorized copying, modification, or distribution is strictly prohibited.
 */
package com.example.dailyhisaab.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dailyhisaab.models.Expense
import com.example.dailyhisaab.utils.AppConstants
import com.example.dailyhisaab.utils.DateUtils

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION) {

    companion object {
        private const val TABLE_CREATE = (
                "CREATE TABLE " + AppConstants.TABLE_EXPENSES + " ("
                        + AppConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + AppConstants.COLUMN_TITLE + " TEXT NOT NULL, "
                        + AppConstants.COLUMN_AMOUNT + " REAL NOT NULL, "
                        + AppConstants.COLUMN_CATEGORY + " TEXT NOT NULL, "
                        + AppConstants.COLUMN_DATE + " TEXT NOT NULL, "
                        + AppConstants.COLUMN_NOTE + " TEXT, "
                        + AppConstants.COLUMN_PAYMENT_METHOD + " TEXT DEFAULT 'Cash', "
                        + AppConstants.COLUMN_TYPE + " TEXT DEFAULT 'EXPENSE'"
                        + ");"
                )
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + AppConstants.TABLE_EXPENSES + " ADD COLUMN " + AppConstants.COLUMN_PAYMENT_METHOD + " TEXT DEFAULT 'Cash'")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + AppConstants.TABLE_EXPENSES + " ADD COLUMN " + AppConstants.COLUMN_TYPE + " TEXT DEFAULT 'EXPENSE'")
        }
    }

    fun insertExpense(expense: Expense): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(AppConstants.COLUMN_TITLE, expense.title)
            put(AppConstants.COLUMN_AMOUNT, expense.amount)
            put(AppConstants.COLUMN_CATEGORY, expense.category)
            put(AppConstants.COLUMN_DATE, expense.date)
            put(AppConstants.COLUMN_NOTE, expense.note)
            put(AppConstants.COLUMN_PAYMENT_METHOD, expense.paymentMethod)
            put(AppConstants.COLUMN_TYPE, expense.type)
        }
        val id = db.insert(AppConstants.TABLE_EXPENSES, null, values)
        db.close()
        return id
    }

    fun updateExpense(id: Long, expense: Expense): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(AppConstants.COLUMN_TITLE, expense.title)
            put(AppConstants.COLUMN_AMOUNT, expense.amount)
            put(AppConstants.COLUMN_CATEGORY, expense.category)
            put(AppConstants.COLUMN_DATE, expense.date)
            put(AppConstants.COLUMN_NOTE, expense.note)
            put(AppConstants.COLUMN_PAYMENT_METHOD, expense.paymentMethod)
            put(AppConstants.COLUMN_TYPE, expense.type)
        }
        val rows = db.update(AppConstants.TABLE_EXPENSES, values, AppConstants.COLUMN_ID + " = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteExpense(id: Long): Int {
        val db = this.writableDatabase
        val rows = db.delete(AppConstants.TABLE_EXPENSES, AppConstants.COLUMN_ID + " = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    val allExpenses: List<Expense>
        get() {
            val expenses = mutableListOf<Expense>()
            val selectQuery = "SELECT * FROM " + AppConstants.TABLE_EXPENSES + " ORDER BY " + AppConstants.COLUMN_DATE + " DESC, " + AppConstants.COLUMN_ID + " DESC"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    expenses.add(getExpenseFromCursor(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return expenses
        }

    fun getFilteredExpenses(query: String?, categoryFilter: String?): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val db = this.readableDatabase

        val selection = StringBuilder()
        val selectionArgs = mutableListOf<String>()

        if (!query.isNullOrBlank()) {
            selection.append("(").append(AppConstants.COLUMN_TITLE).append(" LIKE ? OR ")
                .append(AppConstants.COLUMN_NOTE).append(" LIKE ?)")
            selectionArgs.add("%$query%")
            selectionArgs.add("%$query%")
        }

        if (!categoryFilter.isNullOrBlank() && categoryFilter != "All Categories") {
            if (selection.isNotEmpty()) selection.append(" AND ")
            selection.append(AppConstants.COLUMN_CATEGORY).append(" = ?")
            selectionArgs.add(categoryFilter)
        }

        val selectionStr = if (selection.isNotEmpty()) selection.toString() else null
        val args = if (selectionArgs.isEmpty()) null else selectionArgs.toTypedArray()

        val cursor = db.query(
            AppConstants.TABLE_EXPENSES, null, selectionStr, args, null, null,
            AppConstants.COLUMN_DATE + " DESC, " + AppConstants.COLUMN_ID + " DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                expenses.add(getExpenseFromCursor(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return expenses
    }

    fun getRecentExpenses(limit: Int): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val selectQuery = "SELECT * FROM " + AppConstants.TABLE_EXPENSES + " ORDER BY " + AppConstants.COLUMN_DATE + " DESC, " + AppConstants.COLUMN_ID + " DESC LIMIT " + limit
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                expenses.add(getExpenseFromCursor(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return expenses
    }

    fun getExpenseById(id: Long): Expense? {
        val db = this.readableDatabase
        val cursor = db.query(
            AppConstants.TABLE_EXPENSES, null, AppConstants.COLUMN_ID + " = ?",
            arrayOf(id.toString()), null, null, null
        )

        var expense: Expense? = null
        if (cursor != null && cursor.moveToFirst()) {
            expense = getExpenseFromCursor(cursor)
            cursor.close()
        }
        db.close()
        return expense
    }

    private fun getExpenseFromCursor(cursor: Cursor): Expense {
        val expense = Expense()
        expense.id = cursor.getLong(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_ID))
        expense.title = cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_TITLE))
        expense.amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_AMOUNT))
        expense.category = cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_CATEGORY))
        expense.date = cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.COLUMN_DATE))
        
        val noteIndex = cursor.getColumnIndex(AppConstants.COLUMN_NOTE)
        if (noteIndex != -1 && !cursor.isNull(noteIndex)) {
            expense.note = cursor.getString(noteIndex)
        } else {
            expense.note = ""
        }

        val pmIndex = cursor.getColumnIndex(AppConstants.COLUMN_PAYMENT_METHOD)
        if (pmIndex != -1 && !cursor.isNull(pmIndex)) {
            expense.paymentMethod = cursor.getString(pmIndex)
        } else {
            expense.paymentMethod = "Cash"
        }

        val typeIndex = cursor.getColumnIndex(AppConstants.COLUMN_TYPE)
        if (typeIndex != -1 && !cursor.isNull(typeIndex)) {
            expense.type = cursor.getString(typeIndex)
        } else {
            expense.type = AppConstants.TYPE_EXPENSE
        }

        return expense
    }

    val totalExpense: Double
        get() = getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_EXPENSE + "'")

    val totalIncome: Double
        get() = getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_INCOME + "'")

    val todayExpense: Double
        get() {
            val today = DateUtils.getTodayDate()
            return getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_DATE + " = '" + today + "' AND " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_EXPENSE + "'")
        }

    val monthlyExpense: Double
        get() = getMonthlyExpense(DateUtils.getCurrentMonthPrefix())

    fun getMonthlyExpense(monthPrefix: String): Double {
        return getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_DATE + " LIKE '$monthPrefix%' AND " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_EXPENSE + "'")
    }
    
    fun getMonthlyIncome(monthPrefix: String): Double {
        return getSumAggregate("SELECT SUM(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_DATE + " LIKE '$monthPrefix%' AND " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_INCOME + "'")
    }

    val expenseCount: Int
        get() {
            var count = 0
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT COUNT(*) FROM " + AppConstants.TABLE_EXPENSES, null)
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
            cursor.close()
            db.close()
            return count
        }

    val averageExpense: Double
        get() {
            var avg = 0.0
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT AVG(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_EXPENSE + "'", null)
            if (cursor.moveToFirst()) {
                avg = cursor.getDouble(0)
            }
            cursor.close()
            db.close()
            return avg
        }

    val highestExpense: Double
        get() {
            var max = 0.0
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT MAX(" + AppConstants.COLUMN_AMOUNT + ") FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_EXPENSE + "'", null)
            if (cursor.moveToFirst()) {
                max = cursor.getDouble(0)
            }
            cursor.close()
            db.close()
            return max
        }

    @JvmOverloads
    fun getCategoryWiseTotals(monthPrefix: String? = null): Map<String, Double> {
        val totals = HashMap<String, Double>()
        val db = this.readableDatabase

        var query = "SELECT " + AppConstants.COLUMN_CATEGORY + ", SUM(" + AppConstants.COLUMN_AMOUNT + ") " +
                "FROM " + AppConstants.TABLE_EXPENSES + " WHERE " + AppConstants.COLUMN_TYPE + " = '" + AppConstants.TYPE_EXPENSE + "'"
        if (!monthPrefix.isNullOrEmpty()) {
            query += " AND " + AppConstants.COLUMN_DATE + " LIKE '$monthPrefix%'"
        }
        query += " GROUP BY " + AppConstants.COLUMN_CATEGORY

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(0)
                val total = cursor.getDouble(1)
                totals[category] = total
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return totals
    }

    private fun getSumAggregate(query: String): Double {
        var total = 0.0
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }
}