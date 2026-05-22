package com.project.spendtrackrq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM CategoryTable ORDER BY categoryName ASC")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query(
        """
        SELECT C.*, COUNT(t.categoryName) as transactionCount
        FROM CategoryTable as C
        INNER JOIN TRANSACTIONTABLE as t ON C.categoryName == t.categoryName
        GROUP BY C.categoryName
        HAVING COUNT (t.categoryName) > 0
        ORDER BY transactionCount DESC
    """
    )
    fun getRecentCategory(): Flow<List<CategoryEntity>>

    @Query("SELECT DISTINCT categoryName FROM CategoryTable")
    fun getAllCategoryNames(): Flow<List<String>>

    //Fetch CategoryName, CategoryImageID
    @Query(
        """
        SELECT * FROM CATEGORYTABLE WHERE id == :categoryId
    """
    )
    fun getCategoryById(categoryId: Int): CategoryEntity

}