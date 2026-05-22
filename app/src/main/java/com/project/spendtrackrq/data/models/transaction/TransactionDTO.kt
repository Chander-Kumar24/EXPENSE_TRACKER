package com.project.spendtrackrq.data.models.transaction

import com.project.spendtrackrq.data.models.enums.PaymentType
import com.project.spendtrackrq.data.models.enums.TransactionType

data class TransactionDTO(
    val id: Int,
    val merchantName: String,
    val amount: Float,
    val transactionType: TransactionType,
    val categoryImage: Int,
    val date: String, // can later switch to LocalDate
    val paymentMethod: PaymentType? = null,
    val categoryName: String? = null, // contains name and image

    val transactionId: String? = null, // make nullable

)
