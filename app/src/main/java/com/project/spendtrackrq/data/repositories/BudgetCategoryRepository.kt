package com.project.spendtrackrq.data.repositories

import com.project.spendtrackrq.data.local.dao.BudgetCategoryDAO
import jakarta.inject.Inject

data class BudgetCategoryInfo(
    val categoryId: Int,
    val categoryName: String,
    val imgId: Int,
    val amtAllocated: Float,
    val amtSpent: Float,
)

class BudgetCategoryRepository @Inject constructor(val dao: BudgetCategoryDAO) {
    fun getCategoriesForBudget(budgetId: Int) = dao.getCategoriesForBudget(budgetId)

}
