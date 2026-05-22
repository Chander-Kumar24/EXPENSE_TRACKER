package com.project.spendtrackrq

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.BarEntry
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowGraphs()
{
    val sampleEntries = listOf(
        BarEntry(0f, 4f),
        BarEntry(1f, 8f),
        BarEntry(2f, 6f),
        BarEntry(3f, 2f),
        BarEntry(4f, 7f)
    )
    BarChartView(entries = sampleEntries, label = "Monthly Stats")
}


@Composable
fun BarChartView(entries: List<BarEntry>, label: String) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            BarChart(context).apply {
                // One-time setup
                description.isEnabled = false
                legend.isEnabled = true
                xAxis.apply { position = XAxis.XAxisPosition.BOTTOM; granularity = 1f }
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            // Called on each recomposition (when `entries` changes)
            val dataSet = BarDataSet(entries, label)
            chart.data = BarData(dataSet)
            chart.data?.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}




