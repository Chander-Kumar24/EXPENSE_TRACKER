package com.project.spendtrackrq.data.local.entities.category

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CategoryTable",
    indices = [Index(value = ["categoryName"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryName: String,
    val imgId: Int
)
