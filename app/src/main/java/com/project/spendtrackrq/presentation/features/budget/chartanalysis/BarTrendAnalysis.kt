package com.project.spendtrackrq.presentation.features.budget.chartanalysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.presentation.common.UICommonBottomBar
import com.project.spendtrackrq.presentation.common.UITabRow
import com.project.spendtrackrq.presentation.common.chart.UIBarChart

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBarTrendAnalysis(navController: NavHostController=rememberNavController()) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = { UICommonBottomBar(navController) },
        topBar = {UITabRow(navController = navController,text = "Insight")
        }
    )
    { innerpadding ->

        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        )
        {

            //TODO: Clicking on the bar should show the spending details as details below table of that date
            Text(
                text = "Spending Merchants", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D2B2B),
                modifier = Modifier.padding(top = 16.dp)
            )
            UIBarChart(
                cardModifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 16.dp),
                m_barWidth = 0.2f,
                chartModifier = Modifier.height(180.dp).fillMaxWidth()
                    .padding(top = 24.dp).padding(horizontal = 16.dp),
                labels = listOf(
                    "02/01", "04/01", "11/01", "12/01", "13/01", "14/01", "15/01",
                    "16/01", "17/01", "21/01"
                ),
                yValues = listOf(9f, 12f, 2f, 4f, 6f, 8f, 10f, 12f, 14f, 21f),
                headerContent = {
                    Text(
                        text = "Top merchants in Food & Dining this month",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF6D7280),
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        modifier = Modifier.padding(start = 16.dp).padding(top = 12.dp)
                    )
                },
                footerContent = {}
            )
            UIBarSpendingMerchants()


        }
    }
}

@Composable
fun UIBarSpendingMerchants(
    rows: List<Triple<String, String, String>> = listOf(
        Triple("Burger King", "$477.0", "55.0%"),
        Triple("Wendy's",     "$87.3",  "55.5%"),
        Triple("McDonald's",  "$120.4", "32.1%"),
        Triple("Chiptole",  "$120.4", "32.1%")
    )
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Merchants",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF626464),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                modifier = Modifier.weight(.45f)
            )
            Text(
                text = "Amount",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF626464),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                modifier = Modifier.weight(.30f)
            )
            Text(
                text = "% of Category",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF626464),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                modifier = Modifier.weight(.25f),
                textAlign = TextAlign.End
            )
        }

        rows.forEachIndexed { index, (merchant, amount, percent) ->

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFD4D4D4)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = merchant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    modifier = Modifier.weight(.45f)
                )
                Text(
                    text = amount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    modifier = Modifier.weight(.30f)
                )
                Text(
                    text = percent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    modifier = Modifier.weight(.25f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}