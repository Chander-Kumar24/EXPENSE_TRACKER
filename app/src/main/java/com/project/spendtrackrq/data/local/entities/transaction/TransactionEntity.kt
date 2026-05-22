package com.project.spendtrackrq.data.local.entities.transaction

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.project.spendtrackrq.data.local.entities.budget.BudgetCategoryEntity
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.data.local.entities.user.UserEntity
import com.project.spendtrackrq.data.models.enums.TransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(
    tableName = "TransactionTable",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = BudgetCategoryEntity::class,
            parentColumns = ["budgetId", "categoryId"],
            childColumns = ["budgetId", "categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val merchant: String,
    val budgetId: Int? = null,
    val categoryId: Int,
    val categoryName: String,
    val categoryImage: Int,
    val amount: Float,
    val transactionId: String,
    val timestamp: Long,
    val transactionType: TransactionType,
) {
    // Helper properties
    @delegate:Transient
    val dateObj: Date by lazy { Date(timestamp) }


    @delegate:Transient
    val formattedDate: String by lazy {
        SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
            .format(dateObj)
    }

    @delegate:Transient
    val monthYear: String by lazy {
        SimpleDateFormat("MMM yyyy", Locale.getDefault())
            .format(dateObj)
    }

    @delegate:Transient
    val year: Int by lazy {
        val cal = java.util.Calendar.getInstance()
        cal.time = dateObj
        cal.get(java.util.Calendar.YEAR)
    }

    @delegate:Transient
    val month: Int by lazy {
        val cal = java.util.Calendar.getInstance()
        cal.time = dateObj
        cal.get(java.util.Calendar.MONTH) + 1 // 1-12
    }
}
