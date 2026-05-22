package com.project.spendtrackrq.presentation.common.chart

import android.R
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.project.spendtrackrq.data.models.chart.BarChartDTO
import com.project.spendtrackrq.presentation.features.budget.components.AxisNameFormatter


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowBarChart() {
//    UIBarChart(
//        modifier = Modifier,
//        chartModifier = Modifier.height(300.dp).width(300.dp),
//        labels = listOf("19/01", "20/02", "15/03"),
//        yValues = listOf(9f, 12f, 15f)
//    )
}

@Composable
fun UIBarChart(cardModifier:Modifier,
               chartModifier: Modifier,
               labels: List<String>,
               yValues: List<Float>,
               limitLine: Float=0f,
               m_barWidth: Float = 0.5f,
               headerContent: @Composable () -> Unit = {}, // before chart
               footerContent: @Composable () -> Unit = {}  // after chart
               )
{
    Card(
        modifier = cardModifier,
        border = BorderStroke(1.dp, Color(0x38040404)),
        colors = CardDefaults.cardColors(Color.White),
    )
    {
        headerContent()
        AndroidView(
            modifier=chartModifier.align(Alignment.CenterHorizontally),
            factory = {context ->
                BarChart(context).apply {
                    setDrawBarShadow(false)
                    setDrawValueAboveBar(false)
                    description.isEnabled = false
                    setMaxVisibleValueCount(60)
                    setPinchZoom(false)
                    legend.isEnabled = false
                    setTouchEnabled(false)
                    isDragDecelerationEnabled = false
                    isHighlightPerTapEnabled = false
                    isHighlightPerDragEnabled = false
                    setScaleEnabled(false)

                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.setDrawAxisLine(false)

                    axisLeft.enableGridDashedLine(10f, 10f, 0f)
                    axisLeft.setDrawGridLines(true)

                    axisRight.setDrawGridLines(false)
                    axisRight.spaceBottom = 10f

                    axisLeft.setDrawAxisLine(false)
                    axisLeft.setDrawLabels(true)

                    axisRight.isEnabled = false
                }
            },
            update = { chart ->
                val dataItems = createBarChartData(labels, yValues, chart.context)
                val entries = dataItems.mapIndexed { index, item ->
                    BarEntry(index.toFloat(), item.value)
                }

                if (limitLine != 0f) {
                    val ll = LimitLine(limitLine)
                    ll.lineWidth = 2f
                    ll.enableDashedLine(10f, 10f, 0f)
                    val lineColor = ContextCompat.getColor(chart.context, R.color.darker_gray)

                    ll.lineColor = lineColor
                    ll.lineWidth = 1f
                    chart.axisLeft.addLimitLine(ll)
                }
                chart.axisLeft.setDrawLimitLinesBehindData(true)

                val set1 = BarDataSet(entries, "").apply {
                    setDrawValues(false)
                    colors = dataItems.map { it.color }
                }
                chart.xAxis.valueFormatter = AxisNameFormatter(labels)
                chart.data = BarData(set1).apply {
                    barWidth = m_barWidth
                }
                chart.axisLeft.setLabelCount(3, true)

                chart.axisRight.isEnabled = false
                chart.setFitBars(true)
                chart.axisLeft.axisMinimum = 0f
                chart.xAxis.yOffset = 10f
                chart.xAxis.granularity = 1f
                chart.xAxis.isGranularityEnabled = true
                chart.xAxis.labelCount = labels.size

                chart.invalidate()
            }
        )
        footerContent()
    }

}

fun createBarChartData(xLabels: List<String>, yValues: List<Float>, context: Context)
        : List<BarChartDTO>
{
    // List of colors to be applied to each bar top most 5 colors
    val colors: List<Int> = listOf(
        ContextCompat.getColor(context, R.color.holo_red_light),
        ContextCompat.getColor(context, R.color.holo_blue_light),
        ContextCompat.getColor(context, R.color.holo_green_light),
        ContextCompat.getColor(context, R.color.holo_orange_light),
        ContextCompat.getColor(context, R.color.holo_purple)
    )

    // Now we want to return each BarChart with particular data
    // Each is related to each other
    return xLabels.mapIndexed { index, label ->
        BarChartDTO(
            label = label,
            color = colors[index % colors.size],
            value = yValues.getOrNull(index) ?: 0f,
        )
    }

}