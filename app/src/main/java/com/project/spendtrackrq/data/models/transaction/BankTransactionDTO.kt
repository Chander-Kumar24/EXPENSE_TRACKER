package com.project.spendtrackrq.data.models.transaction

data class BankTransactionDTO(
    val account: String,
    val paymentMethod: String, // should be sep typeOf
    val date: String, //should be typeOf
    val transactionId: String,
    val isDebitCredit: Float,
    val balance: Float
)
