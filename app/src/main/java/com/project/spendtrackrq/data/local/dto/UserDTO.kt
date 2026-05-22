package com.project.spendtrackrq.data.local.dto

data class UserDTO(
    val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val termsAccepted: Boolean
)
