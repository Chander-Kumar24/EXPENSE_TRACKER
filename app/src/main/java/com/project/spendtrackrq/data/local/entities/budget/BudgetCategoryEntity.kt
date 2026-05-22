package com.project.spendtrackrq.data.local.entities.budget

import androidx.room.Entity
import androidx.room.ForeignKey
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity

//Since each category's budget linked to a monthly budget

@Entity(
    tableName = "BudgetCategoryTable",
    primaryKeys = ["budgetId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["budgetId"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetCategoryEntity(
    val budgetId: Int,
    val categoryId: Int,
    val amtAllocated: Float,
    val amtSpent: Float = 0f,
)
