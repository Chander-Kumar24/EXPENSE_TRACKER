package com.project.spendtrackrq.data.models.topbar

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.Dp

data class TopAppBarDTO(
    @DrawableRes val icon: Int,
    val contentDescription: String,
    val onClick: () -> Unit,
    val size: Dp
)