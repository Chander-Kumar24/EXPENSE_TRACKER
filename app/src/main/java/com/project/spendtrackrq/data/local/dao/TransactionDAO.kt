package com.project.spendtrackrq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Update
import com.project.spendtrackrq.data.local.dto.TransactionInfo
import com.project.spendtrackrq.data.local.entities.transaction.TransactionEntity
import com.project.spendtrackrq.data.model.TopCategory
import com.project.spendtrackrq.presentation.features.statsscreen.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    //Insert Transaction
    @Insert(onConflict = IGNORE)
    //there can be multiple transaction of same type
    suspend fun insertTransaction(transaction: TransactionEntity)

    //we want instant change when transaction is added
    @Query("SELECT * FROM TransactionTable ORDER BY timestamp DESC")
    fun getTransaction(): Flow<List<TransactionEntity>>

    @Query(
        """
    SELECT *
    FROM TransactionTable
    WHERE  categoryName = :categoryName
    ORDER BY timestamp DESC
"""
    )
    fun getTransactionsWithCategory(categoryName: String): Flow<List<TransactionEntity>>

    @Query(
        "SELECT * FROM TransactionTable WHERE id = :primaryKey"
    )
    fun getTransactionById(primaryKey: Int): Flow<TransactionEntity>

    @Query(
        """
        DELETE FROM TransactionTable
        WHERE id IN (:transactionIds)
    """
    )
    suspend fun deleteTransaction(transactionIds: List<Int>)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("SELECT sum(amount) FROM TransactionTable WHERE transactionType = 'EXPENSE'")
    fun getTotalExpense(): Flow<Float?> //can be null if its starting

    @Query("SELECT sum(amount) FROM TransactionTable WHERE transactionType = 'INCOME'")
    fun getTotalIncome(): Flow<Float?>

    @Query("SELECT amount, timestamp, transactionType FROM TransactionTable WHERE transactionType = :transactionType")
    fun getTransactionInfoChart(transactionType: TransactionType): Flow<List<TransactionInfo>>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0) 
        FROM TransactionTable 
        WHERE transactionType = 'INCOME' 
        AND strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) = :monthYear
    """
    )
    fun getIncomeByMonth(monthYear: String): Flow<Float>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0) 
        FROM TransactionTable 
        WHERE transactionType = 'EXPENSE' 
        AND strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) = :monthYear
    """
    )
    fun getExpenseByMonth(monthYear: String): Flow<Float>

    @Query(
        """
        SELECT DISTINCT strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) as monthYear
        FROM TransactionTable
        ORDER BY monthYear DESC
    """
    )
    fun getAllAvailableMonths(): Flow<List<String>>

    // Get income for the last 6 months
    @Query(
        """
    SELECT strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) as month,
           COALESCE(SUM(amount), 0) as total
    FROM TransactionTable
    WHERE transactionType = 'INCOME'
    AND date(timestamp / 1000, 'unixepoch') >= date('now', '-6 months')
    GROUP BY month
    ORDER BY month
"""
    )
    fun getIncomeLastSixMonths(): Flow<
            Map<
                    @MapColumn(columnName = "month") String,
                    @MapColumn(columnName = "total") Float
                    >
            >
    // Get expense for the last 6 months
    @Query(
        """
    SELECT strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) as month,
           COALESCE(SUM(amount), 0) as total
    FROM TransactionTable
    WHERE transactionType = 'EXPENSE'
    AND date(timestamp / 1000, 'unixepoch') >= date('now', '-6 months')
    GROUP BY month
    ORDER BY month
"""
    )
    fun getExpenseLastSixMonths(): Flow<Map<
            @MapColumn(columnName = "month") String,
            @MapColumn(columnName = "total") Float
            >
            >

//    @Query("""
//        SELECT categoryId, categoryName, categoryImage, COUNT(*) as transactionCount
//        FROM TransactionTable
//        WHERE transactionType = 'EXPENSE'
//        GROUP BY categoryId
//        ORDER BY transactionCount DESC
//        LIMIT 5
//    """)
//
//    fun getTopCategoriesByUsage(): Flow<List<TopCategory>>

    @Query(
        """
    SELECT categoryId, categoryName, categoryImage, SUM(amount) as totalAmount
    FROM TransactionTable
    WHERE transactionType = 'EXPENSE'
    GROUP BY categoryId
    ORDER BY totalAmount DESC
    LIMIT 5
"""
    )
    fun getTopCategoriesByAmount(): Flow<List<TopCategory>>

    @Query(
        """
    SELECT amount, timestamp, transactionType 
    FROM TransactionTable 
    WHERE transactionType = :transactionType 
    AND strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) = :monthYear
"""
    )
    fun getTransactionInfoByMonth(
        transactionType: TransactionType,
        monthYear: String,
    ): Flow<List<TransactionInfo>>

    @Query(
        """
    SELECT amount, timestamp, transactionType 
    FROM TransactionTable 
    WHERE transactionType = :transactionType 
    AND date(timestamp / 1000, 'unixepoch') >= date('now', '-6 months')
"""
    )
    fun getTransactionInfoLastSixMonths(transactionType: TransactionType): Flow<List<TransactionInfo>>
}