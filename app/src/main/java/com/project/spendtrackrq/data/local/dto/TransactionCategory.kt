package com.project.spendtrackrq.data.local.dto

data class TransactionCategory(
    val merchant: String,
    val date: String,
    val amount: Float,
    val categoryName: String,
    val imgId: Int
)
