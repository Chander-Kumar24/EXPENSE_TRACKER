package com.project.spendtrackrq.data.local.entities.budget

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.project.spendtrackrq.data.local.entities.user.UserEntity

@Entity(
    tableName = "BudgetTable",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId", "year", "month"], unique = true)
    ]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    var budgetId: Int = 0,
    val userId: Int,
    val month: Int,
    val year: Int,   // 2023, 2024
    val allocatedBudget: Float,
    val readyToAllocate: Float,
    val totalSpent: Float = 0f,
) {
    // Helper function to format as MM/YYYY
    fun getFormattedDate(): String {
        return "${month.toString().padStart(2, '0')}/$year"
    }
}
