package com.example.dailyhisaab.utils

object AppConstants {
    const val DATABASE_NAME = "dailyhisaab.db"
    const val DATABASE_VERSION = 4 // Upgraded for Income/Expense tracking

    const val TABLE_EXPENSES = "expenses" // Keeping table name for backward compatibility
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_AMOUNT = "amount"
    const val COLUMN_CATEGORY = "category"
    const val COLUMN_DATE = "date"
    const val COLUMN_NOTE = "note"
    const val COLUMN_PAYMENT_METHOD = "payment_method"
    const val COLUMN_TYPE = "type" // "EXPENSE" or "INCOME"

    const val TYPE_EXPENSE = "EXPENSE"
    const val TYPE_INCOME = "INCOME"

    @JvmField
    val CATEGORIES = listOf(
        "Food", "Travel", "Shopping", "Bills", "Education",
        "Entertainment", "Health", "Fuel", "Other", "Salary", "Investment"
    )
}
