package com.project.spendtrackrq.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.local.entities.transaction.TransactionEntity
import com.project.spendtrackrq.data.models.fab.FABItemsDTO
import com.project.spendtrackrq.data.models.transaction.TransactionDTO
import com.project.spendtrackrq.presentation.features.expense.ExpenseViewModel
import com.project.spendtrackrq.presentation.features.onboard.UserViewModel
import com.project.spendtrackrq.presentation.navigation.components.NavigationScreen
import com.project.spendtrackrq.utils.AllPreviews
import java.util.Calendar
import kotlin.math.abs

@AllPreviews
@Composable
fun ShowCommonHome()
{
    //ICommonHome(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UICommonHome(
    userViewModel: UserViewModel,
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController
) {
    val user by userViewModel.getUserState.collectAsState()
    val expenseVM by expenseViewModel.transactionState.collectAsState()

    val expenseTotal by expenseViewModel.expenseTotal.collectAsState()
    val incomeTotal by expenseViewModel.incomeTotal.collectAsState()

    val (netBalance, expense, income) = remember(expenseTotal, incomeTotal) {
        val expenseValue = expenseTotal ?: 0.0f
        val incomeValue = incomeTotal ?: 0.0f
        val netValue = incomeValue - expenseValue


        Triple(netValue, expenseValue, incomeValue)
    }

    Scaffold(

        floatingActionButton = {
            ExtendedFABAction(navController)
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { UICommonBottomBar(navController) },
        floatingActionButtonPosition = FabPosition.Center
    )
    { innerpadding ->

        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp)
                    .aspectRatio(1.44f)
            ) {
                val (banner, card, notification, _) = createRefs()

                Image(
                    painter = painterResource(R.drawable.homebanner),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .constrainAs(banner) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = "${getCurrentGreeting()},",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 52.dp, start = 16.dp)
                )


                user?.name?.let {
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 72.dp, start = 16.dp)
                    )
                }

                /*Box(
                    modifier=Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .size(40.dp)
                        .constrainAs(genIcon) {
                            top.linkTo(
                                parent.top,
                                margin = 52.dp
                            ) //bring it down by 52dp just at level of good mornin text
                            end.linkTo(notification.start, margin = 16.dp)
                        }
                )
                {
                    Icon(painter = painterResource(R.drawable.sparkles),contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        tint=Color.Unspecified)
                }*/

                Box(
                    modifier=Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .size(40.dp)
                        .constrainAs(notification) {
                            top.linkTo(
                                parent.top,
                                margin = 52.dp
                            ) //bring it down by 52dp just at level of good mornin text
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                )
                {
                    Icon(painter = painterResource(R.drawable.bells),contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        tint=Color.Unspecified)
                }

                // Card Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .constrainAs(card) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(banner.bottom, margin = (-106.dp))
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.frontbanner),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp)
                            .padding(horizontal = 48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Net Expenses",
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = if (netBalance < 0) {
                                "- $${abs(netBalance)}"
                            } else {
                                "$${netBalance}"
                            },
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(24.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.incomicon),
                                contentDescription = "Income",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "Income",
                                color = Color(0xFFD0E5E4),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(Modifier.weight(1f))

                            Icon(
                                painter = painterResource(R.drawable.incomicon),
                                contentDescription = "Expenses",
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(180f),
                                tint = Color.White
                            )
                            Text(
                                text = "Expenses",
                                color = Color(0xFFD0E5E4),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
                            Text(
                                text = "$$income",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = "$$expense",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }

            Column(modifier=Modifier.padding(horizontal = 16.dp))
            {
                val isEmptyList = expenseVM.isEmpty()
                UIHeaderExpendable(
                    navController,
                    navigateTo = if (!isEmptyList) "transactionHistory" else "",
                    "Recent Transactions",
                    if (!isEmptyList) "View All" else ""
                )
                if (isEmptyList) {
                    Text(
                        text = "No Transactions has been added.",
                        color = Color(0xFF666666),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )

                }
                UITxnHistoryCard(
                    /*Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)*/
                    expenseVM
                )

                Row(modifier= Modifier.padding(top=24.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text(
                        text = "Category",
                        color = Color(0xFF222222),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                UICategories(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    navController,
                    expenseViewModel
                )
            }
        }
    }
}

fun getCurrentGreeting(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

@Composable
fun ExtendedFABAction(navHostController: NavHostController)
{
    var expanded by remember { mutableStateOf(false) }

    val fabList = listOf(
        FABItemsDTO(
            R.drawable.savings_active,
            "Set Budget",
            { navHostController.navigate(NavigationScreen.Budget.route) }),
        FABItemsDTO(
            R.drawable.other_active,
            "Add Record",
            { navHostController.navigate("expenseAdder/-1") }),
        // FABItemsDTO(R.drawable.bank_active, "Add Bank Statement",{navHostController.navigate("bankTxnScreen")}),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp))
            {
                fabList.forEach { action->
                    ExtendedFABItems(action, onItemClick = { clickedItem -> clickedItem.onAction()})
                }

            }
        }

        FloatingActionButton(onClick = {expanded = !expanded  },
            shape=CircleShape,
            modifier = Modifier
                .size(70.dp)
                .offset(y = 50.dp),
            containerColor = Color(0xFF438883),
        ) {

            //animate the icon rotation
            val rotation by animateFloatAsState(
                targetValue = if (expanded) 45f else 0f
            )

            Icon(Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .size(36.dp)
                    .rotate(rotation),
                tint = Color.White)
        }


    }

}

@Composable
fun ExtendedFABItems(item: FABItemsDTO, onItemClick: (FABItemsDTO) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onItemClick(item) }
            .padding(4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color=Color.White,//colorScheme.surface
            shadowElevation = 3.dp,
            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = item.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.width(6.dp))
        SmallFloatingActionButton(
            onClick = {onItemClick(item)},
            shape = CircleShape,
            containerColor = Color.White,
            modifier=Modifier.size(36.dp)
        ) {
            Icon(painter =
                painterResource(item.icon),
                contentDescription = item.label,
                tint=Color.Unspecified)
        }
    }
}
@Composable
fun UIHeaderExpendable(
    navController: NavHostController? = null,
    navigateTo: String? = null,
    headerStr: String = "",
    expendable: String = ""
) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = headerStr,
            color = Color(0xFF222222),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.weight(1f))

        val textModifier =
            if (navController != null && !navigateTo.isNullOrEmpty()) {
                Modifier.clickable {
                    navController.navigate(navigateTo)
                }
            } else {
                Modifier
            }

        Text(
            text = expendable,
            modifier = textModifier,
            color = Color(0xFF666666),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}



@Composable
fun UITxnHistoryCard(transactionList: List<TransactionEntity>)
{

    Column(verticalArrangement = Arrangement.spacedBy(8.dp))
    {
        transactionList.take(4).forEach { item ->
            val newTransactionDTO = TransactionDTO(
                id = item.id,
                merchantName = item.merchant,
                categoryImage = item.categoryImage,
                amount = item.amount,
                transactionType = item.transactionType,
                date = item.formattedDate
            )
            UITxnCard(newTransactionDTO, isClickable = false)
        }


    }
}

@Composable
fun UICategories(
    modifier: Modifier,
    navHostController: NavHostController,
    expenseViewModel: ExpenseViewModel
)
{
    //Get me all recent categories
    val getRecentCategories by expenseViewModel.recentTransactionState.collectAsState()

    if (getRecentCategories.isEmpty()) {
        Text(
            text = "No Recent Categories.",
            color = Color(0xFF666666),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

    }
    ExpandableGridSample(modifier, getRecentCategories, navHostController)
}
