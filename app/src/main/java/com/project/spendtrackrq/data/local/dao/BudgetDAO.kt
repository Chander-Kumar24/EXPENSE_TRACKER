package com.project.spendtrackrq.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.spendtrackrq.data.local.entities.budget.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query("SELECT * FROM BudgetTable WHERE budgetId = :budgetId")
    fun getBudgetById(budgetId: Int): Flow<BudgetEntity?>

    @Query("SELECT * FROM BudgetTable WHERE userId = :userId")
    fun getAllBudgets(userId: Int): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM BudgetTable WHERE userId = :userId")
    fun getBudgetByMonth(userId: Int): Flow<List<BudgetEntity>>

    @Query(
        """
        SELECT * FROM BudgetTable 
        WHERE userId = :userId 
        AND month = :month 
        AND year = :year 
        LIMIT 1
    """
    )
    suspend fun getBudgetByMonthAndYear(userId: Int, month: Int, year: Int): BudgetEntity?

    @Query(
        """
        SELECT * FROM BudgetTable 
        WHERE userId = :userId 
        AND year = :year 
        ORDER BY month
    """
    )
    fun getBudgetsByYear(userId: Int, year: Int): Flow<List<BudgetEntity>>

    @Query(
        """
        SELECT * FROM BudgetTable 
        WHERE userId = :userId 
        AND (year > :currentYear OR (year = :currentYear AND month >= :currentMonth))
        ORDER BY year, month
    """
    )
    fun getCurrentAndFutureBudgets(
        userId: Int,
        currentMonth: Int,
        currentYear: Int,
    ): Flow<List<BudgetEntity>>

    @Query(
        """
        SELECT * FROM BudgetTable 
        WHERE userId = :userId 
        AND (year < :currentYear OR (year = :currentYear AND month <= :currentMonth))
        ORDER BY year DESC, month DESC
    """
    )
    fun getPastBudgets(userId: Int, currentMonth: Int, currentYear: Int): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM BudgetTable WHERE userId = :userId ORDER BY year DESC, month DESC")
    fun getAllBudgetsChronological(userId: Int): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM BudgetTable ORDER BY budgetId DESC LIMIT 1")
    fun getLatestBudget(): Flow<BudgetEntity?>

    @Query(
        """
        SELECT * FROM BudgetTable 
        WHERE userId = :userId 
        AND month = :month 
        AND year = :year 
        LIMIT 1
    """
    )
    fun getBudgetByMonthYear(userId: Int, month: Int, year: Int): Flow<BudgetEntity?>
}