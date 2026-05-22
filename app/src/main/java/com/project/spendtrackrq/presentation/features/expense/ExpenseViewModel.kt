package com.project.spendtrackrq.presentation.features.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spendtrackrq.data.local.dao.TransactionDAO
import com.project.spendtrackrq.data.local.dao.UserDAO
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.data.local.entities.transaction.TransactionEntity
import com.project.spendtrackrq.data.models.enums.TransactionType
import com.project.spendtrackrq.data.repositories.BudgetRepository
import com.project.spendtrackrq.data.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class TransactionState(
    val merchant: String = "",
    val amount: Float = 0.00f,
    val userId: Int = 0,
    val date: String = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date()),
    val timestamp: Long = System.currentTimeMillis(),
    val transactionId: String = UUID.randomUUID().toString(),
    val selectedTransactionType: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: CategoryEntity? = null,
) {
    // Parse the current date string to get the timestamp
    private val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

    // Get timestamp from the current date string
    val currentTimestamp: Long
        get() = try {
            dateFormat.parse(date)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }

    fun getFormattedDate(): String {
        return SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
    }

    // Helper function to get start of day timestamp
    fun getStartOfDay(): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    // Helper function to get end of day timestamp
    fun getEndOfDay(): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return cal.timeInMillis
    }
}

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    val transactionDAO: TransactionDAO,
    val userDao: UserDAO,
    private val categoryRepo: CategoryRepository,
    private val budgetRepo: BudgetRepository,
) : ViewModel() {

    //we need to keep state the transaction of current state
    private val _transactionState = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactionState = _transactionState.asStateFlow()

    private val _filteredTransactionState = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val filteredTransactionState = _filteredTransactionState.asStateFlow()

    private val _recentTransactionState = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val recentTransactionState = _recentTransactionState.asStateFlow()

    private val _transaction = MutableStateFlow(TransactionState())
    val transaction = _transaction.asStateFlow()

    val expenseTotal = transactionDAO.getTotalExpense().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0f
    )

    val incomeTotal = transactionDAO.getTotalIncome().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0f
    )

    val getCategories: StateFlow<List<CategoryEntity>> = categoryRepo.getCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        //single source of truth of all transactions
        viewModelScope.launch {
            transactionDAO.getTransaction().collect {
                _transactionState.value = it
            }
        }
        viewModelScope.launch {
            categoryRepo.getRecentCategories().collect {
                _recentTransactionState.value = it
            }
        }
    }
    fun onNameChange(name: String) {
        _transaction.value = _transaction.value.copy(merchant = name)
    }

    fun onDateChange(date: String) {
        _transaction.value = _transaction.value.copy(date = date)
    }

    fun onTabChange(selectedTab: TransactionType) {
        _transaction.value = _transaction.value.copy(selectedTransactionType = selectedTab)
    }

    fun onCategoryChange(category: CategoryEntity) {
        _transaction.value = _transaction.value.copy(selectedCategory = category)

    }

    fun onAmountChange(amt: Float) {
        _transaction.value = _transaction.value.copy(amount = amt)
    }

    fun onTransactionIdChange(id: String) {
        _transaction.value = _transaction.value.copy(transactionId = id)
    }

    fun loadTransactionByCategory(category: String) {
        viewModelScope.launch {
            transactionDAO.getTransactionsWithCategory(category).collect {
                _filteredTransactionState.value = it
            }
        }
    }

    fun getTransactionById(primaryKey: Int): Flow<TransactionEntity?> {
        //this is just one time operation
        return if (primaryKey == -1) {
            flowOf(null)
        } else {
            transactionDAO.getTransactionById(primaryKey)
        }
    }

    fun updateTransaction(transactionId: Int) {
        viewModelScope.launch {
            try {
                val currentState = _transaction.value
                val originalTransaction = transactionDAO.getTransactionById(transactionId).first()

                val updatedTransaction = originalTransaction.copy(
                    merchant = currentState.merchant.trim(),
                    amount = currentState.amount,
                    timestamp = currentState.currentTimestamp,
                    categoryName = currentState.selectedCategory?.categoryName ?: "",
                    categoryId = currentState.selectedCategory?.id ?: 0,
                    categoryImage = currentState.selectedCategory?.imgId ?: 0,
                    transactionType = currentState.selectedTransactionType
                )

                transactionDAO.updateTransaction(updatedTransaction)
                _eventFlow.emit("Transaction updated successfully!")

            } catch (e: Exception) {
                _eventFlow.emit("Error: ${e.message ?: "Failed to update transaction"}")
            }
        }
    }

    fun deleteTransaction(deleteTransactionId: List<Int>) {
        viewModelScope.launch {
            try {
                transactionDAO.deleteTransaction(deleteTransactionId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Add single transaction
    fun addTransaction() {
        val currentState = _transaction.value

        // Input validation
        if (currentState.merchant.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit("Merchant cannot be empty")
            }
            return
        }

        if (currentState.selectedCategory == null) {
            viewModelScope.launch {
                _eventFlow.emit("Category cannot be empty")
            }
            return
        }

        viewModelScope.launch {
            try {
                val currentUser = userDao.observeUser().first()
                    ?: throw IllegalStateException("No user found. Cannot save transaction.")

                //1. Fetch if budget is created.
                //2. if so then check if the category which we adding is there if so
                //3. link transactions to it and add the amount to it
                //4. update the category parameter's as we go

                val budgetId = budgetRepo.getLatestBudget().first()?.budgetId

                val newTransaction = TransactionEntity(
                    budgetId = budgetId,
                    userId = currentUser.id,
                    merchant = currentState.merchant.trim(),
                    amount = currentState.amount,
                    timestamp = currentState.currentTimestamp,
                    categoryName = currentState.selectedCategory.categoryName,
                    categoryId = currentState.selectedCategory.id,
                    categoryImage = currentState.selectedCategory.imgId,
                    transactionType = currentState.selectedTransactionType,
                    transactionId = currentState.transactionId
                )


                transactionDAO.insertTransaction(newTransaction)

                budgetId?.let { id ->
                    budgetRepo.updateBudgetSpentAmount(
                        budgetId = id,
                        categoryId = currentState.selectedCategory.id,
                        amountToAdd = currentState.amount
                    )

                }

                // Reset the form after successful save
                _transaction.value = TransactionState()
                _eventFlow.emit("Transaction added successfully!")

            } catch (e: Exception) {
                _eventFlow.emit("Error: ${e.message ?: "Failed to add transaction"}")
            }
        }
    }
}