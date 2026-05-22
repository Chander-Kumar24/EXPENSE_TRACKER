package com.project.spendtrackrq.data.repositories

import androidx.room.Transaction
import com.project.spendtrackrq.data.local.dao.BudgetCategoryDAO
import com.project.spendtrackrq.data.local.dao.BudgetDAO
import com.project.spendtrackrq.data.local.entities.budget.BudgetCategoryEntity
import com.project.spendtrackrq.data.local.entities.budget.BudgetEntity
import com.project.spendtrackrq.presentation.features.budget.BudgetCategoryUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDAO,
    private val budgetCategoryDao: BudgetCategoryDAO,
) {
    @Transaction
    suspend fun addBudgetWithCategory(
        month: Int,
        year: Int,
        allocatedBudget: Float,
        readyToAllocate: Float,
        totalSpent: Float,
        userId: Int,
        categories: List<BudgetCategoryUI>,
    ): Long {
        // Check if budget already exists for this month/year
        val existingBudget = budgetDao.getBudgetByMonthAndYear(userId, month, year)

        val budget = BudgetEntity(
            userId = userId,
            month = month,
            year = year,
            allocatedBudget = allocatedBudget,
            readyToAllocate = readyToAllocate,
            totalSpent = totalSpent
        )

        val budgetId = if (existingBudget != null) {
            budget.budgetId = existingBudget.budgetId
            budgetDao.updateBudget(budget)
            existingBudget.budgetId.toLong()
        } else {
            budgetDao.insertBudget(budget)
        }

        val existingCategories = if (existingBudget != null) {
            budgetCategoryDao.getAllBudgetCategoryByBudgetId(existingBudget.budgetId).first()
        } else {
            emptyList()
        }

        val categoryEntities = categories.map { categoryUI ->
            val existingCategory =
                existingCategories.find { it.categoryId == categoryUI.selectedCategoryID }
            BudgetCategoryEntity(
                budgetId = budgetId.toInt(),
                categoryId = categoryUI.selectedCategoryID,
                amtAllocated = categoryUI.categoryAmount.toFloatOrNull() ?: 0f,
                amtSpent = existingCategory?.amtSpent ?: 0f
            )
        }

        budgetCategoryDao.deleteBudgetCategories(budgetId.toInt())
        budgetCategoryDao.addBudgetCategory(categoryEntities)

        return budgetId
    }

    fun getBudgetByMonthYear(userId: Int, month: Int, year: Int): Flow<BudgetEntity?> {
        return budgetDao.getBudgetByMonthYear(userId, month, year)
    }

    fun getBudgetsByYear(userId: Int, year: Int): Flow<List<BudgetEntity>> {
        return budgetDao.getBudgetsByYear(userId, year)
    }

    fun getCurrentAndFutureBudgets(
        userId: Int,
        currentMonth: Int,
        currentYear: Int,
    ): Flow<List<BudgetEntity>> {
        return budgetDao.getCurrentAndFutureBudgets(userId, currentMonth, currentYear)
    }

    fun getPastBudgets(userId: Int, currentMonth: Int, currentYear: Int): Flow<List<BudgetEntity>> {
        return budgetDao.getPastBudgets(userId, currentMonth, currentYear)
    }

    fun getAllBudgetsChronological(userId: Int): Flow<List<BudgetEntity>> {
        return budgetDao.getAllBudgetsChronological(userId)
    }

    fun getLatestBudget(): Flow<BudgetEntity?> {
        return budgetDao.getLatestBudget()
    }
    suspend fun updateBudgetSpentAmount(budgetId: Int, categoryId: Int, amountToAdd: Float) {
        // Get the current budget and category details
        val budget = budgetDao.getBudgetById(budgetId).first() ?: return
        val categories = budgetCategoryDao.getAllBudgetCategoryByBudgetId(budgetId).first()
        val category = categories.find { it.categoryId == categoryId } ?: return

        val availableInCategory = (category.amtAllocated - category.amtSpent).coerceAtLeast(0f)

        // Determine how much we need to take from the provisional balance
        val amountToTakeFromProvisional = (amountToAdd - availableInCategory).coerceAtLeast(0f)

        val newTotalSpent = budget.totalSpent + amountToAdd
        val newReadyToAllocate =
            (budget.readyToAllocate - amountToTakeFromProvisional).coerceAtLeast(0f)

        budgetDao.updateBudget(
            budget.copy(
                totalSpent = newTotalSpent,
                readyToAllocate = newReadyToAllocate
            )
        )
        budgetCategoryDao.addExpenseToBudgetCategory(budgetId, categoryId, amountToAdd)
    }

    suspend fun deleteBudget(budgetId: Int) {
        budgetCategoryDao.deleteBudgetCategories(budgetId)

        val budget = budgetDao.getBudgetById(budgetId).first()
        budget?.let { budgetDao.deleteBudget(it) }
    }

}