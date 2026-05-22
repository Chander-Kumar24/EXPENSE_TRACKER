package com.project.spendtrackrq.data.models.fab

data class FABItemsDTO(
    val icon: Int,
    val label: String,
    val onAction: () -> Unit = {}
)