package com.project.spendtrackrq.data.state

data class RegistrationUIState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val errorMessage: String? = null
)
