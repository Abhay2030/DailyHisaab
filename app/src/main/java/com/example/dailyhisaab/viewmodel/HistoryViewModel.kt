package com.example.dailyhisaab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyhisaab.database.DatabaseHelper
import com.example.dailyhisaab.models.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class HistoryUiState(
    val transactions: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val categoryFilter: String = "All Categories",
    val isLoading: Boolean = false
)

class HistoryViewModel(
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val state = _uiState.value
            val result = withContext(Dispatchers.IO) {
                if (state.searchQuery.isBlank() && state.categoryFilter == "All Categories") {
                    dbHelper.allExpenses
                } else {
                    dbHelper.getFilteredExpenses(state.searchQuery, state.categoryFilter)
                }
            }
            _uiState.value = _uiState.value.copy(transactions = result, isLoading = false)
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadTransactions()
    }

    fun updateCategoryFilter(category: String) {
        _uiState.value = _uiState.value.copy(categoryFilter = category)
        loadTransactions()
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.deleteExpense(id)
            loadTransactions()
        }
    }
    
    fun restoreTransaction(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.insertExpense(expense)
            loadTransactions()
        }
    }
}

class HistoryViewModelFactory(
    private val dbHelper: DatabaseHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
