package com.example.dailyhisaab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyhisaab.database.DatabaseHelper
import com.example.dailyhisaab.models.Expense
import com.example.dailyhisaab.utils.AppConstants
import com.example.dailyhisaab.utils.DateUtils
import com.example.dailyhisaab.utils.SettingsManager
import com.example.dailyhisaab.utils.ValidationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AddTransactionUiState(
    val id: Long = -1,
    val type: String = AppConstants.TYPE_EXPENSE,
    val amount: String = "",
    val title: String = "",
    val category: String = "",
    val paymentMethod: String = "Cash",
    val date: String = DateUtils.getTodayDate(),
    val note: String = "",
    
    val titleError: String? = null,
    val amountError: String? = null,
    val categoryError: String? = null,
    val dateError: String? = null,
    
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val saveError: String? = null
)

class AddTransactionViewModel(
    private val dbHelper: DatabaseHelper,
    private val settingsManager: SettingsManager,
    private val transactionId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        if (transactionId != -1L) {
            loadTransaction(transactionId)
        } else {
            // Pre-select last category
            val lastCat = settingsManager.lastCategory
            if (lastCat != null) {
                _uiState.value = _uiState.value.copy(category = lastCat)
            }
        }
    }

    private fun loadTransaction(id: Long) {
        viewModelScope.launch {
            val expense = withContext(Dispatchers.IO) { dbHelper.getExpenseById(id) }
            if (expense != null) {
                _uiState.value = _uiState.value.copy(
                    id = expense.id,
                    type = expense.type,
                    amount = if (expense.amount > 0) expense.amount.toString() else "",
                    title = expense.title,
                    category = expense.category,
                    paymentMethod = expense.paymentMethod,
                    date = expense.date,
                    note = expense.note
                )
            }
        }
    }

    fun updateType(type: String) { _uiState.value = _uiState.value.copy(type = type) }
    fun updateAmount(amount: String) { _uiState.value = _uiState.value.copy(amount = amount, amountError = null) }
    fun updateTitle(title: String) { _uiState.value = _uiState.value.copy(title = title, titleError = null) }
    fun updateCategory(category: String) { _uiState.value = _uiState.value.copy(category = category, categoryError = null) }
    fun updatePaymentMethod(method: String) { _uiState.value = _uiState.value.copy(paymentMethod = method) }
    fun updateDate(date: String) { _uiState.value = _uiState.value.copy(date = date, dateError = null) }
    fun updateNote(note: String) { _uiState.value = _uiState.value.copy(note = note) }

    fun saveTransaction() {
        val state = _uiState.value
        
        var hasError = false
        var titleErr: String? = null
        var amountErr: String? = null
        var categoryErr: String? = null
        var dateErr: String? = null

        if (!ValidationHelper.isValidTitle(state.title)) {
            titleErr = "Title cannot be empty"
            hasError = true
        }
        if (!ValidationHelper.isValidAmount(state.amount)) {
            amountErr = "Enter a valid positive amount"
            hasError = true
        }
        if (!ValidationHelper.isValidCategory(state.category)) {
            categoryErr = "Please select a category"
            hasError = true
        }
        if (!ValidationHelper.isValidDate(state.date)) {
            dateErr = "Please select a date"
            hasError = true
        }

        if (hasError) {
            _uiState.value = state.copy(
                titleError = titleErr,
                amountError = amountErr,
                categoryError = categoryErr,
                dateError = dateErr
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, saveError = null)
            
            val expense = Expense(
                id = state.id,
                title = state.title.trim(),
                amount = state.amount.toDouble(),
                category = state.category,
                date = state.date,
                note = state.note.trim(),
                paymentMethod = state.paymentMethod,
                type = state.type
            )

            val result = withContext(Dispatchers.IO) {
                if (state.id != -1L) {
                    dbHelper.updateExpense(state.id, expense).toLong()
                } else {
                    dbHelper.insertExpense(expense)
                }
            }

            if (result != -1L && result != 0L) {
                settingsManager.lastCategory = state.category
                _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
            } else {
                _uiState.value = _uiState.value.copy(isSaving = false, saveError = "Failed to save transaction")
            }
        }
    }
}

class AddTransactionViewModelFactory(
    private val dbHelper: DatabaseHelper,
    private val settingsManager: SettingsManager,
    private val transactionId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTransactionViewModel(dbHelper, settingsManager, transactionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
