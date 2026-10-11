package com.example.dailyhisaab.models

import java.io.Serializable

data class Expense(
    var id: Long = -1,
    var title: String = "",
    var amount: Double = 0.0,
    var category: String = "",
    var date: String = "", // YYYY-MM-DD
    var note: String = "",
    var paymentMethod: String = "Cash",
    var type: String = "EXPENSE" // Default to EXPENSE to preserve existing functionality
) : Serializable
