package com.project.spendtrackrq.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.spendtrackrq.data.local.CategoryObject
import com.project.spendtrackrq.data.local.dao.BudgetCategoryDAO
import com.project.spendtrackrq.data.local.dao.BudgetDAO
import com.project.spendtrackrq.data.local.dao.CategoryDAO
import com.project.spendtrackrq.data.local.dao.TransactionDAO
import com.project.spendtrackrq.data.local.dao.UserDAO
import com.project.spendtrackrq.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

//TODO: Bindings for Room database

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        categoryDaoProvider: Provider<CategoryDAO>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "BudgetAppDatabase.db"
        )
            .fallbackToDestructiveMigration(false)
            .addCallback(PrepopulateCallback(categoryDaoProvider))
            .build()
    }
    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDAO {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDAO {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: AppDatabase): TransactionDAO {
        return database.transactionDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database: AppDatabase): BudgetDAO {
        return database.budgetDao()
    }


    @Provides
    @Singleton
    fun provideBudgetCategoryDao(database: AppDatabase): BudgetCategoryDAO {
        return database.budgetCategoryDao()
    }
}

class PrepopulateCallback(
    private val categoryDaoProvider: Provider<CategoryDAO>
) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateCategories()
        }
    }

    private suspend fun populateCategories() {
        val categoryDao = categoryDaoProvider.get()
        categoryDao.insertAllCategories(CategoryObject.PREPOPULATE_CATEGORIES)
    }
}