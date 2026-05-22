package com.project.spendtrackrq.data.local.dto

import com.project.spendtrackrq.presentation.features.statsscreen.TransactionType

data class TransactionInfo(
    val amount: String,
    val timestamp: Long,
    val transactionType: TransactionType,
)
