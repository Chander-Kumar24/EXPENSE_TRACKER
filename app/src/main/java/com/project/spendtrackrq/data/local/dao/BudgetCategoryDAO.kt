package com.project.spendtrackrq.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.project.spendtrackrq.data.local.entities.budget.BudgetCategoryEntity
import com.project.spendtrackrq.data.models.BudgetWithCategory
import com.project.spendtrackrq.data.repositories.BudgetCategoryInfo
import kotlinx.coroutines.flow.Flow


@Dao
interface BudgetCategoryDAO {

    @Insert
    suspend fun addBudgetCategory(budgetCategory: List<BudgetCategoryEntity>)

    @Delete
    suspend fun deleteBudgetCategory(budgetCategory: BudgetCategoryEntity)

    @Query("DELETE FROM BudgetCategoryTable WHERE budgetId = :budgetId")
    suspend fun deleteBudgetCategories(budgetId: Int)

    @Query("UPDATE BudgetCategoryTable SET amtSpent = amtSpent + :spentAmount WHERE budgetId = :budgetId AND categoryId = :categoryId")
    suspend fun addExpenseToBudgetCategory(budgetId: Int, categoryId: Int, spentAmount: Float)

    @Query("SELECT * FROM BudgetCategoryTable WHERE budgetId = :budgetId")
    fun getAllBudgetCategoryByBudgetId(budgetId: Int): Flow<List<BudgetCategoryEntity>>

    @Query(
        """
        SELECT * FROM BudgetCategoryTable bc
        INNER JOIN CategoryTable c ON bc.categoryId = c.id
        WHERE bc.budgetId = :budgetId
    """
    )
    fun getBudgetWithCategories(budgetId: Int): Flow<List<BudgetWithCategory>>

    @Query(
        """
        SELECT 
            c.id AS categoryId,
            c.categoryName AS categoryName,
            c.imgId AS imgId,
            bc.amtAllocated AS amtAllocated,
            bc.amtSpent AS amtSpent
        FROM BudgetCategoryTable AS bc
        INNER JOIN CategoryTable AS c
            ON bc.categoryId = c.id
        WHERE bc.budgetId = :budgetId
    """
    )
    fun getCategoriesForBudget(budgetId: Int): Flow<List<BudgetCategoryInfo>>
}