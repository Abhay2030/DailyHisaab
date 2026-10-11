package com.example.dailyhisaab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyhisaab.database.DatabaseHelper
import com.example.dailyhisaab.models.Expense
import com.example.dailyhisaab.utils.InsightsHelper
import com.example.dailyhisaab.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class HomeUiState(
    val totalExpense: Double = 0.0,
    val todayExpense: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val totalIncome: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyBudget: Float = 0f,
    val recentTransactions: List<Expense> = emptyList(),
    val insights: List<String> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val dbHelper: DatabaseHelper,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadData(context: android.content.Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val totalExp = withContext(Dispatchers.IO) { dbHelper.totalExpense }
            val todayExp = withContext(Dispatchers.IO) { dbHelper.todayExpense }
            val monthExp = withContext(Dispatchers.IO) { dbHelper.monthlyExpense }
            val totalInc = withContext(Dispatchers.IO) { dbHelper.totalIncome }
            
            // Reusing existing DB method logic for current month
            val monthPrefix = com.example.dailyhisaab.utils.DateUtils.getCurrentMonthPrefix()
            val monthInc = withContext(Dispatchers.IO) { dbHelper.getMonthlyIncome(monthPrefix) }
            
            val recent = withContext(Dispatchers.IO) { dbHelper.getRecentExpenses(5) } // Fetch 5 instead of 3
            val budget = settingsManager.monthlyBudget
            val insightsList = withContext(Dispatchers.IO) { InsightsHelper.generateInsights(context, dbHelper) }

            _uiState.value = HomeUiState(
                totalExpense = totalExp,
                todayExpense = todayExp,
                monthlyExpense = monthExp,
                totalIncome = totalInc,
                monthlyIncome = monthInc,
                monthlyBudget = budget,
                recentTransactions = recent,
                insights = insightsList,
                isLoading = false
            )
        }
    }
}

class HomeViewModelFactory(
    private val dbHelper: DatabaseHelper,
    private val settingsManager: SettingsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dbHelper, settingsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
