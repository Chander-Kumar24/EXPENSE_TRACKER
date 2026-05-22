package com.project.spendtrackrq.presentation.features.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spendtrackrq.data.local.dao.BudgetCategoryDAO
import com.project.spendtrackrq.data.local.dao.UserDAO
import com.project.spendtrackrq.data.local.entities.budget.BudgetEntity
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.data.repositories.BudgetRepository
import com.project.spendtrackrq.data.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetCategoryUI(
    val categoryAmount: String = "",
    val selectedCategoryID: Int = 0,
)

sealed class BudgetUIState {
    object Loading : BudgetUIState()
    data class HasBudget(val budget: BudgetEntity) : BudgetUIState()
    object NoBudget : BudgetUIState()
    data class Error(val message: String) : BudgetUIState()
}

data class BudgetState(
    val currentAmount: Float = 0.0f,
    val readyToAllocate: Float = 0.0f,
    val date: String = "",
    val budgetPeriod: String = "",
    val totalSpent: Float = 0.0f,
    val selectedMonth: Int = 0, // 1-12, 0 means not selected
    val selectedYear: Int = 0,  // e.g., 2023, 2024, etc.

    val budgetCategories: List<BudgetCategoryUI> = emptyList(),
    val isDialogVisible: Boolean = false,
    val isDialogEditable: Boolean = false,
    val dialogSelectedCategory: CategoryEntity? = null,
    val dialogAmount: String = "",
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    val userDao: UserDAO,
    val categoryRepo: CategoryRepository,
    val budgetCategoryRepo: BudgetCategoryDAO,
    private val budgetRepo: BudgetRepository,
) : ViewModel() {

    //we need to store current budget

    private val _budgetState = MutableStateFlow(BudgetState())
    val budgetState = _budgetState.asStateFlow()

    private val _budgetUIState = MutableStateFlow<BudgetUIState>(BudgetUIState.Loading)
    val budgetUIState: StateFlow<BudgetUIState> = _budgetUIState.asStateFlow()

    val readBudgetState = budgetRepo.getLatestBudget().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // In BudgetViewModel.kt
    @OptIn(ExperimentalCoroutinesApi::class)
// In BudgetViewModel.kt
    val budgetWithCategories = combine(
        readBudgetState,
        readBudgetState
            .filterNotNull()
            .map { it.budgetId }
            .distinctUntilChanged()
            .flatMapLatest { budgetId ->
                budgetCategoryRepo.getAllBudgetCategoryByBudgetId(budgetId)
            }
    ) { budget, categories ->
        budget to categories
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null to emptyList()
    )

    init {
        loadLatestBudget()
    }

    fun loadBudgetForEditing(budget: BudgetEntity) {
        viewModelScope.launch {
            try {
                val categories =
                    budgetCategoryRepo.getAllBudgetCategoryByBudgetId(budget.budgetId).first()
                val budgetCategories = categories.map {
                    BudgetCategoryUI(
                        categoryAmount = it.amtAllocated.toString(),
                        selectedCategoryID = it.categoryId
                    )
                }

                _budgetState.update { currentState ->
                    currentState.copy(
                        currentAmount = budget.allocatedBudget,
                        selectedMonth = budget.month,
                        selectedYear = budget.year,
                        budgetCategories = budgetCategories,
                        readyToAllocate = budget.allocatedBudget - budgetCategories.sumOf {
                            (it.categoryAmount.toFloatOrNull() ?: 0f).toInt()
                        }
                    )
                }
            } catch (e: Exception) {
                // Handle error
                _budgetUIState.value = BudgetUIState.Error(e.message ?: "Error loading budget data")
            }
        }
    }

    fun isBudgetExpired(month: Int, year: Int): Boolean {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val currentMonth = java.util.Calendar.getInstance()
            .get(java.util.Calendar.MONTH) + 1 // Adding 1 because Calendar.MONTH is 0-based

        return year < currentYear || (year == currentYear && month < currentMonth)
    }

    private fun loadLatestBudget() {
        viewModelScope.launch {
            try {
                budgetRepo.getLatestBudget().collect { budget ->
                    _budgetUIState.value = if (budget != null) {
                        if (isBudgetExpired(budget.month, budget.year)) {
                            BudgetUIState.NoBudget
                        } else {
                            BudgetUIState.HasBudget(budget)
                        }
                    } else {
                        BudgetUIState.NoBudget
                    }
                }
            } catch (e: Exception) {
                _budgetUIState.value = BudgetUIState.Error(e.message ?: "Error loading budget")
            }
        }
    }


    //this need to be effected by another flow so we combine two flows here
    val availableGetCategory = categoryRepo.getCategories().combine(_budgetState)
    { alreadyCategory, currentBudgetState ->

        val currentAddedCategory = currentBudgetState.budgetCategories.map { it.selectedCategoryID }
        alreadyCategory.filterNot { it.id in currentAddedCategory }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val getCategories: StateFlow<List<CategoryEntity>> = categoryRepo.getCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onAmountChange(amount: Float) {

        _budgetState.update {
            it.copy(
                currentAmount = amount,
                readyToAllocate = amount -
                        it.budgetCategories.sumOf { cat ->
                            cat.categoryAmount.toFloatOrNull()?.toDouble() ?: 0.0
                        }.toFloat()

            )
        }

    }

    fun onEditCategoryDialog(item: CategoryEntity?, amountEdit: String) {
        _budgetState.update {
            it.copy(
                isDialogVisible = true,
                isDialogEditable = true,
                dialogSelectedCategory = item,
                dialogAmount = amountEdit
            )
        }
    }


    fun onDeleteCategoryDialog() {
        val getCurrentBudgetStatus = _budgetState.value
        val categoryToDelete = getCurrentBudgetStatus.dialogSelectedCategory

        if (categoryToDelete == null) return

        val delCategory = getCurrentBudgetStatus.budgetCategories.find {
            it.selectedCategoryID == categoryToDelete.id
        }

        val updatedList = getCurrentBudgetStatus.budgetCategories.filterNot {
            it.selectedCategoryID == categoryToDelete.id
        }
        val amtToAdd = delCategory?.categoryAmount
        _budgetState.update {
            it.copy(
                readyToAllocate = it.readyToAllocate + (amtToAdd?.toFloat() ?: 0f),
                budgetCategories = updatedList,
                isDialogVisible = false,
                dialogSelectedCategory = null,
                dialogAmount = ""
            )
        }
    }

    fun onDateChange(month: Int, year: Int) {
        val date = "${month.toString().padStart(2, '0')}/$year"
        _budgetState.update {
            it.copy(
                date = date,
                selectedMonth = month,
                selectedYear = year
            )
        }
    }


    fun onShowAddCategoryDialog(isEdit: Boolean = false) {
        _budgetState.update { it.copy(isDialogVisible = true, isDialogEditable = isEdit) }
    }

    fun onDismissDialog() {
        _budgetState.update {
            it.copy(
                isDialogVisible = false,
                dialogSelectedCategory = null,
                isDialogEditable = false,
                dialogAmount = ""
            )
        }
    }

    fun getCategoriesByBudget(budgetId: Int) = budgetCategoryRepo.getCategoriesForBudget(budgetId)




    fun onDialogAmountChange(amount: String) {
        _budgetState.update { it.copy(dialogAmount = amount) }
    }

    fun onDialogCategorySelected(category: CategoryEntity) {
        _budgetState.update { it.copy(dialogSelectedCategory = category) }
    }

    fun onConfirmDialogCategory() {
        val currentState = _budgetState.value
        val dialogCategory = currentState.dialogSelectedCategory
        val dialogAmount = currentState.dialogAmount.toFloatOrNull()

        if (dialogCategory == null || dialogAmount == null || dialogAmount <= 0) {
            //Emit a event here
            return
        }

        //Now we need to add the BudgetCategoryEntity to list
        //Each time we call it we create enw model
        val newBudgetCategory = BudgetCategoryUI(
            categoryAmount = dialogAmount.toString(),
            selectedCategoryID = dialogCategory.id
        )
        val existsCategory =
            currentState.budgetCategories.any { it.selectedCategoryID == newBudgetCategory.selectedCategoryID }

        val updatedList = if (existsCategory) {
            currentState.budgetCategories.map { category ->
                if (category.selectedCategoryID == currentState.dialogSelectedCategory.id) {
                    category.copy(categoryAmount = dialogAmount.toString())
                } else {
                    category
                }
            }
        } else {
            currentState.budgetCategories + newBudgetCategory
        }.toList()

        // we add to list with budgetState
        _budgetState.update {
            it.copy(
                readyToAllocate = it.readyToAllocate - dialogAmount,
                budgetCategories = updatedList,
                isDialogVisible = false,
                dialogSelectedCategory = null,
                dialogAmount = ""
            )
        }
    }

    fun onConfirmBudget() {

        //get me current budget state
        val getCurrentBudgetState = _budgetState.value

        //TODO: Precheck here before inserting

        viewModelScope.launch {
            val getCurrentUser = userDao.observeUser().first()
            budgetRepo.addBudgetWithCategory(
                userId = getCurrentUser?.id ?: 0,
                month = getCurrentBudgetState.selectedMonth,
                year = getCurrentBudgetState.selectedYear,
                allocatedBudget = getCurrentBudgetState.currentAmount,
                readyToAllocate = getCurrentBudgetState.readyToAllocate,
                totalSpent = getCurrentBudgetState.totalSpent,
                categories = getCurrentBudgetState.budgetCategories
            )
        }


    }

    /**
     * Delete the current budget and all its categories
     */
    fun deleteBudget() {
        viewModelScope.launch {
            try {
                // Get the current budget ID
                val currentBudget = readBudgetState.value
                currentBudget?.let { budget ->
                    // Delete the budget and its categories
                    budgetRepo.deleteBudget(budget.budgetId)
                    // Update the UI state to reflect the deletion
                    _budgetUIState.value = BudgetUIState.NoBudget
                }
            } catch (e: Exception) {
                // Handle any errors that might occur during deletion
                _budgetUIState.value = BudgetUIState.Error(e.message ?: "Failed to delete budget")
            }
        }
    }
}