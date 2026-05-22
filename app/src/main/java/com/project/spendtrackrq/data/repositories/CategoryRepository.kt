package com.project.spendtrackrq.data.repositories

import com.project.spendtrackrq.data.local.dao.CategoryDAO
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDAO: CategoryDAO) {
    fun getCategories(): Flow<List<CategoryEntity>> = categoryDAO.getCategories()
    fun getRecentCategories(): Flow<List<CategoryEntity>> = categoryDAO.getRecentCategory()
    fun getCategoryInfo(categoryId: Int): CategoryEntity {
        return categoryDAO.getCategoryById(categoryId)

    }


}