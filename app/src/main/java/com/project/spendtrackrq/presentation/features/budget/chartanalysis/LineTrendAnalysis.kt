package com.project.spendtrackrq.presentation.features.budget.chartanalysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.models.chart.BarChartLegendDTO
import com.project.spendtrackrq.data.models.chart.LineChartDataDTO
import com.project.spendtrackrq.presentation.common.UICommonBottomBar
import com.project.spendtrackrq.presentation.common.UITabRow
import com.project.spendtrackrq.presentation.common.chart.BrandList
import com.project.spendtrackrq.presentation.common.chart.UILineChartData

@Preview(showSystemUi = true, showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UILineTrendAnalysis(navController: NavHostController=rememberNavController()) {
    Scaffold(
        topBar = {UITabRow(navController=navController,text = "Insights")},
        bottomBar = { UICommonBottomBar(navController) },
    )
    { innerpadding ->

        Column(
            modifier = Modifier
                .padding(innerpadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {
            val brands = listOf(
                BarChartLegendDTO("Burger King", Color(0xFFF44336)),  // Red
                BarChartLegendDTO("MacDonald's", Color(0xFFFFA726)), // Orange
                BarChartLegendDTO("Wendy’s", Color(0xFF8BC34A)), // Green
            )
            Text(
                text = "Trend Analysis",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D2B2B),
                modifier = Modifier.padding(top = 36.dp)
            )
            UILineChartData(
                cardModifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 16.dp),
                chartDataList = listOf(
                    LineChartDataDTO(
                        points = listOf(
                            "28/05" to 400f,
                            "29/05" to 800f,
                            "30/05" to 1200f,
                            "31/05" to 1600f
                        ),
                        label = "Line 1",
                        colorRes = R.color.teal_200
                    )
                ),
                xGridLines = false,
                graphModifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                ylabelCount = 5,
                xlabelCount = 5,
                headerContent = {},
                footerContent = {
                    Spacer(modifier = Modifier.height(8.dp)) // spacing from chart
                    BrandList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        items = brands
                    )

                }


            )

            Text(
                text = "Merchants",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D2B2B),
                modifier = Modifier.padding(top = 36.dp)
            )

            Column(modifier=Modifier
                .fillMaxHeight()
                .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp))
            {
                // UITxnHistoryCard(Modifier.fillMaxWidth())

            }

            UISummaryCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(150.dp)
            )

            /* //Removing for now, To much to do
            Text(
                text="Weekly Spending",
                fontSize=24.sp,
                fontWeight = FontWeight.Bold,
                color=Color(0xFF2D2B2B),
                modifier = Modifier.padding(top=16.dp)
            )*/


        }
    }
}

@Composable
fun UISummaryCard(modifier: Modifier=Modifier)
{
    Card(modifier=modifier,
        border = BorderStroke(0.5.dp, Color(0xFFDFDFDF)),
        colors = CardDefaults.cardColors(Color.White)
    )
    {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Icon in circle background

            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sum_info),
                    contentDescription = "Summary Icon"
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text Column
            Column {
                Text(
                    text = "Summary",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0x70000000)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "• McDonald's (orange line) saw a significant rise, peaking around 900 near the end of July, then experiencing a sharp drop after July 28.\n" +
                            "• Wendy's (green line) started lower but rose steadily throughout the month, surpassing Burger King and McDonald's near the end of July.\n" +
                            "• Burger King (red/purple lines) had a smaller increase.",
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Medium,
                    color = Color(0x40000000)
                )
            }
        }
    }
}