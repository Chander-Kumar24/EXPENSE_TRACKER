package com.project.spendtrackrq.data.local.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val termsAccepted: Boolean = false,
    val completedOnboarding: Boolean = false
)
