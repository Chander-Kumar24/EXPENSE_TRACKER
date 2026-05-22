package com.project.spendtrackrq.presentation.features.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.models.chart.BarChartLegendDTO
import com.project.spendtrackrq.data.models.chart.LineChartDataDTO
import com.project.spendtrackrq.data.models.topbar.TopAppBarDTO
import com.project.spendtrackrq.presentation.common.MyCustomProgressBar
import com.project.spendtrackrq.presentation.common.UICommonBottomBar
import com.project.spendtrackrq.presentation.common.UITabRow
import com.project.spendtrackrq.presentation.common.chart.BrandList
import com.project.spendtrackrq.presentation.common.chart.UILineChartData
import com.project.spendtrackrq.presentation.features.budget.components.card.UIBudgetCatCard
import com.project.spendtrackrq.utils.AllPreviews

@AllPreviews
@Composable
fun ShowMainBudgetScreen()
{
    //UIMainBudgetScreen(rememberNavController())
}

@Composable
fun UIMainBudgetScreen(
    navController: NavHostController,
    budgetViewModel: BudgetViewModel,
    onEditClick: () -> Unit = {},
    isEditMode: Boolean = false,
) {
    val editAction = TopAppBarDTO(
        R.drawable.edit,
        contentDescription = "Edit",
        onClick = {
            navController.navigate("setBudgetScreen?isEdit=true") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        size = 18.dp
    )

    val actions = listOf(editAction)
    Scaffold(
        containerColor = Color.White,
        topBar = {
            UITabRow(
                navController,
                "Budget",
                isVisible = true,
                actions = actions
            )
        },
        bottomBar = { UICommonBottomBar(navController) },
    )
    { innerpadding ->
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            ){
            UIBudgetMainScreen(budgetViewModel, navController)
            //TODO: Add Time Pill chip just like before with month week and custom
            //Uncomment it to add monthly weekly since cant decide how will it look like
           /* var selectedIndex by remember { mutableStateOf(0) }
            PillSelector(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                options = listOf("Month"), //Monthly, weekly, custom
                selectedIndex = selectedIndex,
                onOptionSelected = { index -> selectedIndex = index }
            )*/

        }
    }
}

@Composable
fun UIBudgetMainCard(budgetViewModel: BudgetViewModel)
{
    val budgetAndCategories by budgetViewModel.budgetWithCategories.collectAsState()

    val allocated = budgetAndCategories.first?.allocatedBudget ?: 0f
    val totalAllocatedCategories =
        budgetAndCategories.second.sumOf { it.amtAllocated.toDouble() }.toFloat()
    val totalSpentCategories = budgetAndCategories.second.sumOf { it.amtSpent.toDouble() }.toFloat()

    // Budget remaining is the total allocated minus what's been spent
    val budgetRemaining = (allocated - totalSpentCategories).coerceAtLeast(0f)
    // Provisional balance is the amount not yet allocated to any category
    val provisionalBalance = (allocated - totalAllocatedCategories).coerceAtLeast(0f)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0x38040404)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
    Column(modifier=Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                text="Monthly Budget",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6E6C6C),
            )

            Text(
                text = budgetAndCategories.first?.allocatedBudget.toString(),
                fontSize = 32.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier=Modifier.padding(top=2.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text="Amount Spent",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF626464),
                    modifier = Modifier.weight(1f)
                )


                Text(
                    text = totalSpentCategories.toString(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF626464),
                    modifier = Modifier.weight(1f), // Also takes the same space
                    textAlign = TextAlign.End
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text="Budget Remaining",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B4B4B),
                    modifier = Modifier.weight(1f)
                )


                Text(
                    text = String.format("%.2f", budgetRemaining),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B4B4B),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Provisional Balance",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B4B4B),
                    modifier = Modifier.weight(1f)
                )


                Text(
                    text = String.format("%.2f", provisionalBalance),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B4B4B),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            val progress = if (allocated > 0) {
                (totalSpentCategories / allocated).coerceIn(0f, 1f)
            } else {
                0f
            }

            MyCustomProgressBar(
                progress = progress,
                horizontalPadding = 16
            )

            Text(
                text = "${(progress * 100).toInt()}% spent",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.Normal,
                color = Color(0xFF626464),
                modifier = Modifier.padding(top = 8.dp)
            )

        }
    }

}


@Composable
fun UIBudgetMainScreen(
    budgetViewModel: BudgetViewModel,
    navController: NavHostController,
) {
    UIBudgetMainCard(budgetViewModel)
    val (budget, _) = budgetViewModel.budgetWithCategories.collectAsState().value

    val categoryList by budgetViewModel
        .getCategoriesByBudget(budget?.budgetId ?: 0)
        .collectAsState(initial = emptyList())
    
    Text(
        text = "Categories",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2D2B2B),
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        categoryList.forEach { item ->
            val progress = if (item.amtAllocated == 0f) {
                0f
            } else {
                (item.amtSpent / item.amtAllocated).coerceIn(0f, 1f)
            }
            UIBudgetCatCard(
                cardModifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                catName = item.categoryName,
                spentAmt = item.amtSpent.toInt(),
                remAmt = (item.amtAllocated - item.amtSpent).toInt(),
                totalAmt = item.amtAllocated.toInt(),
                progress = progress,
                categoryIcon = item.imgId,
                onClick = {
                    navController.navigate("transactionHistory?category=${item.categoryName}")
                }
            )
        }
    }

}

//Parameters will be flexible
@Composable
fun UIBudgetOtherScreens()
{
    //UIBudgetMainCard()

    val brands = listOf(
        BarChartLegendDTO("Burger King", Color(0xFFF44336)),  // Red
        BarChartLegendDTO("MacDonald's", Color(0xFFFFA726)), // Orange
        BarChartLegendDTO("Wendy’s", Color(0xFF8BC34A)), // Green
        BarChartLegendDTO("Wendy’s", Color(0xFF8BC34A)),
        BarChartLegendDTO("Wendy’s", Color(0xFF8BC34A)),
    )


    Text(
        text = "Trend Analysis",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2D2B2B),
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    )

    UILineChartData(
        cardModifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
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
            .fillMaxWidth()
            .aspectRatio(1.75f)
            .padding(top = 8.dp),
        ylabelCount = 5,
        xlabelCount = 5,
        headerContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "July 2025",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF222222),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_forward),
                        tint=Color.Unspecified,
                        contentDescription = "Open",
                        modifier=Modifier.fillMaxSize(0.7f)
                    )
                }
            }

        },
        footerContent = {
            //It will house the legends
            Spacer(modifier = Modifier.height(12.dp))
            BrandList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 8.dp),
                items = brands
            )
        }
    )

}



