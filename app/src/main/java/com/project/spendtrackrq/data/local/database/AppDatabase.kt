package com.project.spendtrackrq.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.spendtrackrq.data.local.dao.BudgetCategoryDAO
import com.project.spendtrackrq.data.local.dao.BudgetDAO
import com.project.spendtrackrq.data.local.dao.CategoryDAO
import com.project.spendtrackrq.data.local.dao.TransactionDAO
import com.project.spendtrackrq.data.local.dao.UserDAO
import com.project.spendtrackrq.data.local.entities.budget.BudgetCategoryEntity
import com.project.spendtrackrq.data.local.entities.budget.BudgetEntity
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.data.local.entities.transaction.TransactionEntity
import com.project.spendtrackrq.data.local.entities.user.UserEntity

// Room database definition

@Database(
    entities = [
        UserEntity::class,
        BudgetEntity::class,
        BudgetCategoryEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun transactionDao(): TransactionDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun budgetDao(): BudgetDAO
    abstract fun budgetCategoryDao(): BudgetCategoryDAO

}