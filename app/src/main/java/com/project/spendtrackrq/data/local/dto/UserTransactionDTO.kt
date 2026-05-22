package com.project.spendtrackrq.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.project.spendtrackrq.data.local.entities.transaction.TransactionEntity
import com.project.spendtrackrq.data.local.entities.user.UserEntity

//Gives me user detail with transaction, in one-go
data class UserTransactionDTO(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "userId", //of userEntity
        entityColumn = "userId"
    )
    val transactions: List<TransactionEntity>
)
