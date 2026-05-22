package com.project.spendtrackrq.presentation.features.budget.components

import com.github.mikephil.charting.formatter.ValueFormatter

class AxisNameFormatter(private val labels: List<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index in labels.indices) labels[index] else ""
    }
}