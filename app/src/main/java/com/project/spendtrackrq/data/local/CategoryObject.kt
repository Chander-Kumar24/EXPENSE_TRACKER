package com.project.spendtrackrq.data.local

import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity

object CategoryObject {
    val PREPOPULATE_CATEGORIES = listOf(
        CategoryEntity(0, "Food", R.drawable.food_active),
        CategoryEntity(0, "Entertainment", R.drawable.entertainment_active),
        CategoryEntity(0, "Bills", R.drawable.bills_active),
        CategoryEntity(0, "Travel", R.drawable.travel_active),
        CategoryEntity(0, "Shopping", R.drawable.shopping_active),
        CategoryEntity(0, "Health", R.drawable.health_active),
        CategoryEntity(0, "Business", R.drawable.business_active),
        CategoryEntity(0, "Other", R.drawable.other_active),
    )
}