package com.project.spendtrackrq.data.models

import androidx.room.Embedded
import androidx.room.Relation
import com.project.spendtrackrq.data.local.entities.budget.BudgetCategoryEntity
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity

data class BudgetWithCategory(
    @Embedded
    val budgetCategory: BudgetCategoryEntity,

    @Relation(
        parentColumn = "categoryId",
        entity = CategoryEntity::class,
        entityColumn = "id"
    )
    val category: CategoryEntity,
)
