package com.project.spendtrackrq.presentation.common.chart

import android.R
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.project.spendtrackrq.data.models.chart.LineChartDataDTO
import com.project.spendtrackrq.presentation.features.budget.components.AxisNameFormatter
import kotlin.collections.forEach

fun createLineData(
    chartDataList: List<LineChartDataDTO>,
    context: Context,
    xLabels: List<String>
): LineData {
    val dataSets = ArrayList<ILineDataSet>()

    chartDataList.forEach { lineChartData ->
        val entries = lineChartData.points.map { (label, y) ->
            val index = xLabels.indexOf(label) // map label to position
            Entry(index.toFloat(), y)
        }

        val lineDataSet = LineDataSet(entries, lineChartData.label).apply {
            setDrawValues(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.LINEAR
            lineWidth = 2f
            color = ContextCompat.getColor(context, lineChartData.colorRes)
            setDrawHighlightIndicators(true)
            highLightColor = ContextCompat.getColor(context, R.color.holo_red_light)
            highlightLineWidth = 1f
        }

        dataSets.add(lineDataSet)
    }

    return LineData(dataSets)
}


@Composable
fun UILineChartData(cardModifier:Modifier,
                    graphModifier:Modifier,
                    chartDataList: List<LineChartDataDTO>,
                    xGridLines: Boolean=true,
                    limitLine: Float=0f,
                    ylabelCount: Int = 0,
                    xlabelCount: Int = 0,
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
        Column()
        {
            headerContent()
            AndroidView(
                modifier = graphModifier
                    .align(Alignment.CenterHorizontally),
                factory = { context ->
                    LineChart(context).apply {
                        description.isEnabled = false
                        legend.isEnabled = false
                        setPinchZoom(false)
                        setTouchEnabled(false)
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        isDragDecelerationEnabled = false
                        isHighlightPerTapEnabled = false
                        isHighlightPerDragEnabled = false
                        setScaleEnabled(false)
                        axisLeft.setDrawAxisLine(false)
                        axisLeft.setDrawLabels(true)
                        axisLeft.enableGridDashedLine(10f, 10f, 0f)
                        axisLeft.setDrawGridLines(true)

                    }
                },
                update = { chart ->
                    if (limitLine != 0f) {
                        val ll = LimitLine(limitLine)
                        ll.lineWidth = 2f
                        ll.enableDashedLine(10f, 10f, 0f)
                        val lineColor = ContextCompat.getColor(chart.context, R.color.darker_gray)

                        ll.lineColor = lineColor
                        ll.lineWidth = 1f
                        chart.axisLeft.addLimitLine(ll)
                    }

                    val xLabels = chartDataList
                        .flatMap { it.points.map { point -> point.first } }

                    chart.xAxis.valueFormatter = AxisNameFormatter(xLabels)
                    chart.data = createLineData(chartDataList, chart.context, xLabels)

                    chart.axisLeft.setDrawLimitLinesBehindData(true)

                    if (ylabelCount != 0)
                        chart.axisLeft.setLabelCount(ylabelCount, true)
                    if (xlabelCount != 0)
                        chart.xAxis.setLabelCount(xlabelCount, true)

                    chart.axisRight.isEnabled = false
                    chart.axisLeft.axisMinimum = 0f
                    chart.xAxis.setDrawAxisLine(false)
                    //chart.xAxis.xOffset = 10f
                    chart.xAxis.yOffset = 10f

                    chart.xAxis.setDrawGridLines(xGridLines)
                    chart.xAxis.enableGridDashedLine(10f, 10f, 0f)
                    chart.xAxis.granularity = 1f
                    chart.xAxis.isGranularityEnabled = true
                    chart.invalidate()
                }
            )
            footerContent()
        }


    }

}