package com.project.spendtrackrq.presentation.common.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.spendtrackrq.data.models.chart.BarChartLegendDTO

@Preview
@Composable
fun ShowBrandList()
{
    val brands = listOf(
        BarChartLegendDTO("Burger King", Color(0xFFF44336)),
        BarChartLegendDTO("Mac'Donalds", Color(0xFFFFA726)),
        BarChartLegendDTO("Wendy’s", Color(0xFF8BC34A)),
        BarChartLegendDTO("Taco Bell", Color(0xFF4CAF50)),
        BarChartLegendDTO("KFC", Color(0xFFFFEB3B)),
        BarChartLegendDTO("Pizza Hut", Color(0xFF2196F3)),
        BarChartLegendDTO("Subway", Color(0xFF9C27B0))
    )
    Column(modifier = Modifier.padding(16.dp)) {
        BrandList(Modifier, brands)
    }
}
@Composable
fun BrandList(modifier: Modifier, items: List<BarChartLegendDTO>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {

        val itemsToShow = items.take(5)

        itemsToShow.forEach { brand ->

        BrandItemView(brand)
        }
    }
}


@Composable
fun BrandItemView(brand: BarChartLegendDTO) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(brand.color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        BasicText(
            text = brand.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )
        )
    }
}
