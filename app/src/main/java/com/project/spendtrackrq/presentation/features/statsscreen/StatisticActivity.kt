package com.project.spendtrackrq.presentation.features.statsscreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.models.chart.BarChartLegendDTO
import com.project.spendtrackrq.data.models.chart.LineChartDataDTO
import com.project.spendtrackrq.presentation.common.PillDateSelector
import com.project.spendtrackrq.presentation.common.UICommonBottomBar
import com.project.spendtrackrq.presentation.common.UITabRow
import com.project.spendtrackrq.presentation.common.chart.BrandList
import com.project.spendtrackrq.presentation.common.chart.UIBarChart
import com.project.spendtrackrq.presentation.common.chart.UILineChartData
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowStatsActivity()
{
    UIStatsActivity(rememberNavController()) //just for working
}

private fun getCurrentMonthName(): String {
    return SimpleDateFormat("MMM", Locale.getDefault()).format(Date())
}

private fun getLastMonthName(): String {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.MONTH, -1)
    }
    return SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UIStatsActivity(
    navController: NavHostController,
    viewModel: StatisticViewModel = hiltViewModel(),
) {

    val currentMonthIncome by viewModel.currentMonthIncome.collectAsStateWithLifecycle()
    val currentMonthExpense by viewModel.currentMonthExpense.collectAsStateWithLifecycle()
    val incomePercentageChange by viewModel.incomePercentageChange.collectAsStateWithLifecycle()
    val expensePercentageChange by viewModel.expensePercentageChange.collectAsStateWithLifecycle()
    val currentMonth = getCurrentMonthName()
    val lastMonth = getLastMonthName()

    val dateRangeOptions = listOf(
        currentMonth,
        lastMonth,
        "6 Months",
        "Max"
    )
    val vmBrandList =
        viewModel.categoryWithColors.collectAsStateWithLifecycle(initialValue = emptyMap())
    val vmCategories =
        viewModel.topCategories.collectAsStateWithLifecycle(initialValue = emptyList())

    val finalBrandList = vmBrandList.value.map { it ->
        BarChartLegendDTO(it.key, Color(it.value))
    }

    val selectedDateRange by viewModel.selectedDateRange.collectAsStateWithLifecycle()

    val selectedIndex = when (selectedDateRange) {
        DateRange.CurrentMonth -> 0
        DateRange.LastMonth -> 1
        DateRange.LastSixMonths -> 2
        DateRange.AllTime -> 3
        else -> 0
    }

    // Chart data
    val lineChartList = viewModel.lineChart.collectAsStateWithLifecycle(initialValue = emptyList())
    val chartType by viewModel.lineChartType.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { UICommonBottomBar(navController) },
        topBar = { UITabRow(navController,"Statistics")}
    )
    { innerpadding ->

        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        )
        {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                )
                {
                    UIInsightCards(
                        Modifier
                            .padding(top = 36.dp)
                            .weight(1f),
                        imgId = R.drawable.other_income_active,
                        delta = incomePercentageChange,
                        amt = currentMonthIncome,
                        name = "Total Income"
                    )
                    UIInsightCards(
                        Modifier
                            .padding(top = 36.dp)
                            .weight(1f),
                        imgId = R.drawable.bonus_active,
                        delta = expensePercentageChange,
                        amt = currentMonthExpense,
                        name = "Total Expense"
                    )
                }

                /*HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp), thickness = 2.dp
                )*/
            val lineChartPoints = lineChartList.value
                Text(
                    text = "Trend Analysis",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    modifier = Modifier.padding(top = 36.dp)
                )
            val lineChartVM = viewModel.lineChartType.collectAsStateWithLifecycle()
            val lineChartDate = viewModel.selectedDateRange.collectAsStateWithLifecycle()

            UILineChartData(
                cardModifier = Modifier
                    .fillMaxWidth()
                    .height(410.dp)
                    .padding(top = 16.dp),
                chartDataList = listOf(
                    LineChartDataDTO(
                        points = lineChartPoints,
                        label = if (lineChartVM.value == TransactionType.INCOME) "Income" else "Expense",
                        colorRes = if (lineChartVM.value == TransactionType.INCOME)
                            R.color.green_500 else R.color.red_500
                    )
                ),
                xGridLines = lineChartDate.value !is DateRange.CurrentMonth,
                graphModifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .height(200.dp),
                    ylabelCount = 3,
                    xlabelCount = 3,
                    headerContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, end = 16.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            TextOnlyDropdownM3(
                                modifier = Modifier.padding(top = 8.dp),
                                transactionType = chartType,
                                statsViewModel = viewModel
                            )
                        }
                        /* Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(horizontal = 16.dp)
                                 .padding(top = 16.dp),
                             verticalAlignment = Alignment.CenterVertically
                         )
                         {
                             Text(
                                 text = "$3,400",
                                 fontSize = 30.sp,
                                 fontFamily = FontFamily(Font(R.font.inter_medium)),
                                 fontWeight = FontWeight.SemiBold,
                                 color = Color(0xFF222222),
                             )


                             Row(
                                 verticalAlignment = Alignment.CenterVertically,
                             )
                             {

                                 Image(
                                     painter = painterResource(R.drawable.system_icons),
                                     contentDescription = null,
                                     modifier = Modifier.size(15.dp)
                                 )
                                 Text(
                                     modifier = Modifier
                                         .align(Alignment.Bottom)
                                         .padding(bottom = 2.dp),
                                     text = "21% last month",
                                     fontSize = 10.sp,
                                     fontFamily = FontFamily(Font(R.font.inter_medium)),
                                     fontWeight = FontWeight.Medium,
                                     color = Color(0xFF16A34A),
                                 )

                             }
                             Spacer(modifier = Modifier.weight(1f))

                         }*/

                    },
                    footerContent = {
                        PillDateSelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 65.dp)
                                .padding(top = 24.dp)
                                .padding(horizontal = 16.dp),
                            options = dateRangeOptions,
                            selectedIndex = selectedIndex,
                            onSelect = { index ->
                                val newDateRange = when (index) {
                                    0 -> DateRange.CurrentMonth
                                    1 -> DateRange.LastMonth
                                    2 -> DateRange.LastSixMonths
                                    else -> DateRange.AllTime
                                }
                                viewModel.setDateRange(newDateRange)
                            }
                        )

                        // Summary Notes info
                        /*  Row(
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .padding(top = 16.dp),
                              horizontalArrangement = Arrangement.Center,
                              verticalAlignment = Alignment.CenterVertically
                          )
                          {
                              Image(
                                  painter = painterResource(R.drawable.info),
                                  contentDescription = null,
                                  modifier = Modifier.size(10.dp)
                              )
                              Spacer(modifier = Modifier.width(4.dp))
                              Text(
                                  text = "You spent more 60% on Food Categories than Auto.",
                                  fontSize = 10.sp,
                                  fontFamily = FontFamily(Font(R.font.inter_medium)),
                                  fontWeight = FontWeight.Medium,
                                  color = Color(0xFF9A9A9A),
                              )
                          }*/

                    }


                )
                /*HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp), thickness = 2.dp
                )*/
                Text(
                    text = "Top Categories",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    modifier = Modifier.padding(top = 36.dp)
                )
            //Just show the top 5 categories spending wise
                UIBarChart(
                    cardModifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    m_barWidth = 0.2f,
                    chartModifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.75f)// Make the chart's height 1.75 times its width. Or use 1f for a square
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    labels = List(vmCategories.value.size) { "" },
                    yValues = vmCategories.value.map { it.totalAmount.toFloat() },
                    headerContent = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(
                                text = "Top Categories By Expense",
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF222222),
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            /*IconButton(onClick = {}) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_forward),
                                    tint=Color.Unspecified,
                                    contentDescription = "Open",
                                    modifier=Modifier.fillMaxSize(0.7f)
                                )
                            }*/
                        }

                    },
                    footerContent = {
                        //It will house the legends

                        Spacer(modifier = Modifier.height(12.dp)) // spacing from chart
                        BrandList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp),
                            items = finalBrandList
                        )

                    }
                )

                Spacer(modifier = Modifier.height(16.dp)) // spacing from chart



        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextOnlyDropdownM3(
    modifier: Modifier = Modifier,
    transactionType: TransactionType,
    statsViewModel: StatisticViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Income", "Expense")

    // Animation for chevron rotation
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = modifier//.wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (transactionType == TransactionType.INCOME) "Income" else "Expense",
                style = MaterialTheme.typography.bodyLarge
            )

            // Chevron icon with rotation animation
            Icon(
                painter = painterResource(R.drawable.droparrow), // positional parameter
                contentDescription = "Dropdown arrow",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(10.dp)
                    .rotate(rotationAngle)
            )

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        // Set the transaction type based on the selected item
                        val newType =
                            if (item == "Income") TransactionType.INCOME else TransactionType.EXPENSE
                        statsViewModel.setLineChartType(newType)
                        expanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun UIInsightCards(
    modifier: Modifier,
    imgId: Int = 0,
    delta: Float = 0f,
    name: String = "",
    amt: Float = 0f,
)
{
    val formattedDelta = if (delta % 1f == 0f) {

        String.format("%02d", abs(delta).toInt())
    } else {

        String.format("%.1f", abs(delta))
    }
    Card(modifier=modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        shape= RoundedCornerShape(8.dp),
        border=BorderStroke(1.dp, Color(0x38040404))
    )
    {
        Column(modifier=Modifier
            .fillMaxSize()
            .align(Alignment.Start)
            .padding(top = 4.dp)
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            Image(painter = painterResource(imgId),
                contentDescription = null,
                modifier=Modifier.size(50.dp)
                )

            Text(
                text = name,
                fontFamily= FontFamily(Font(R.font.inter_medium)),
                color=Color(0xFF666666),
                fontSize = 16.sp
            )

            Text(
                "$${NumberFormat.getNumberInstance(Locale.US).format(amt)}",
                fontFamily= FontFamily(Font(R.font.inter_medium)),
                color=Color(0xFF070707),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
                )
            {
                Image(painter = painterResource(R.drawable.system_icons),contentDescription = null,
                colorFilter = if (delta < 0) ColorFilter.tint(Color.Red) else null,
                    modifier = Modifier.rotate(if (delta < 0) 90f else 0f)  )

                BasicText(
                    text="$formattedDelta% last month",
                    style = LocalTextStyle.current.copy(
                        fontFamily=FontFamily(Font(R.font.inter_medium)),
                        color = if (delta < 0) Color.Red else Color(0xFF16A34A),
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 12.sp,
                        maxFontSize = 18.sp
                    )
                )


                /*Text(
                    text = "$formattedDelta% last month",
                    fontFamily=FontFamily(Font(R.font.inter_medium)),
                    color = if (delta < 0) Color.Red else Color(0xFF16A34A),
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    softWrap = false,
                    modifier=Modifier.fillMaxWidth()

                )*/

            }

        }

    }
}







