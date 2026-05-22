package com.project.spendtrackrq.data.models.chart

data class LineChartDataDTO(
    val points: List<Pair<String, Float>>,
    val label: String,
    val colorRes: Int
)
