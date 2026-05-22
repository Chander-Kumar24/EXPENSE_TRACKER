package com.project.spendtrackrq.presentation.features.budget

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.presentation.common.MyCustomProgressBar
import com.project.spendtrackrq.presentation.common.UICommonTxnCard
import com.project.spendtrackrq.presentation.common.UIMonthPicker
import com.project.spendtrackrq.presentation.features.budget.components.dialog.UICategoryAddBudgetDialog
import com.project.spendtrackrq.presentation.features.expense.InvoiceAdder
import com.project.spendtrackrq.presentation.features.expense.PriceWithClearButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UISetBudgetScreen(
    navController: NavHostController? = null,
    budgetViewModel: BudgetViewModel,
    isEdit: Boolean = false,
) {
    val budgetViewModelState by budgetViewModel.budgetState.collectAsState()
    val getCategory by budgetViewModel.availableGetCategory.collectAsState()
    val allCategories by budgetViewModel.getCategories.collectAsState()

    // Get the latest budget to check if we're editing an existing budget
    val latestBudget by budgetViewModel.readBudgetState.collectAsState()

    // Load existing budget data when the screen is first displayed
    LaunchedEffect(latestBudget) {
        latestBudget?.let { budget ->
            budgetViewModel.loadBudgetForEditing(budget)
        }
    }

    // Get the current budget state
    val budgetState by budgetViewModel.budgetState.collectAsState()

    // Update the UI when the budget state changes
    LaunchedEffect(budgetState) {
        // This will ensure the UI updates when the budget state changes
    }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            UIBudgetTopAppBar(navController) {
                budgetViewModel.onConfirmBudget()
                // Navigate back after saving
                navController?.popBackStack()
            }
        }

    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "READY TO ALLOCATE",
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = Color(0xFF666666),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = if (budgetViewModelState.readyToAllocate < 0) {
                                "- $${abs(budgetViewModelState.readyToAllocate)}"
                            } else {
                                "$${budgetViewModelState.readyToAllocate}"
                            },
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = Color.Black,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Text(
                    text = "AMOUNT",
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color(0xFF666666),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                PriceWithClearButton(
                    budgetViewModelState.currentAmount,
                    { budgetViewModel.onAmountChange(it) })

                Text(
                    text = "BUDGET PERIOD",
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color(0xFF666666),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))
                UIMonthPicker(
                    selectedMonth = budgetViewModelState.selectedMonth,
                    selectedYear = budgetViewModelState.selectedYear,
                    onMonthSelected = { month, year ->
                        budgetViewModel.onDateChange(month, year)
                    }
                )

                Text(
                    text = "CATEGORY",
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color(0xFF666666),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))


                if (!getCategory.isEmpty()) {
                    InvoiceAdder("Add Category") {
                        budgetViewModel.onShowAddCategoryDialog()
                    }
                }
                if (budgetViewModelState.isDialogVisible) {
                    Dialog(onDismissRequest = { budgetViewModel.onDismissDialog() }) {
                        UICategoryAddBudgetDialog(
                            categories = getCategory,
                            isEditing = budgetViewModelState.isDialogEditable,
                            onCategoryChange = { budgetViewModel.onDialogCategorySelected(it) },
                            onAmountChange = { budgetViewModel.onDialogAmountChange(it) },
                            amount = budgetViewModelState.dialogAmount,
                            selectedCategory = budgetViewModelState.dialogSelectedCategory,
                            onDelete = {
                                budgetViewModel.onDeleteCategoryDialog()
                            },
                            onDismiss = { budgetViewModel.onDismissDialog() },
                            onConfirm = {
                                budgetViewModel.onConfirmDialogCategory()
                            }
                        )
                    }
                }
                getCategory.forEach { item ->
                    Log.d("CATEGORY", item.categoryName)
                }

                Spacer(modifier = Modifier.height(18.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 8.dp)
                )
                {
                    budgetViewModelState.budgetCategories.forEach { item ->

                        val categoryItem =
                            allCategories.firstOrNull { it.id == item.selectedCategoryID }
                                ?: return@forEach //skip this item only that we wrote above, 'labeled return'
                        Log.d("SELECTED", item.selectedCategoryID.toString())
                        UIBudgetCategory(
                            categoryItem,
                            item.categoryAmount,
                            budgetViewModel
                        )

                    }

                }

                if (isEdit) {
                    Spacer(modifier = Modifier.height(16.dp))
                    UIDeleteBudgetButton {
                        // Launch delete operation in a coroutine scope
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                budgetViewModel.deleteBudget()
                                navController?.popBackStack()
                            } catch (e: Exception) {
                                // Handle any errors here if needed
                                Log.e("SetBudgetScreen", "Error deleting budget", e)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Composable
fun UIBudgetTopAppBar(navController: NavHostController? = null, functionCall: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.Center
    )
    {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    val popped = navController?.popBackStack()
                    if (popped == false) navController.navigate("Home")
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Set Budget",
                    color = Color(0xFF666666),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Box(
                modifier = Modifier.align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        functionCall()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.done),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }


        }
    }
}


@Composable
fun UIBudgetCategory(
    categoryItem: CategoryEntity,
    amount: String,
    budgetViewModel: BudgetViewModel,
) {
    UICommonTxnCard(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth()
            .height(80.dp)
            .combinedClickable(
                onClick = {
                    //Here i want to open the edit dialog box
                    budgetViewModel.onEditCategoryDialog(categoryItem, amount)
                }
            ),
        //cardBgColor = Color(0xFFFBFBFB),
        cardBgColor = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Color.Transparent, //Color(0xFFF0F6F5)
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            )
            {
                Image(
                    painter = painterResource(id = categoryItem.imgId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = categoryItem.categoryName,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF000000)
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "$$amount",
                color = Color(0xFF4B4B4B),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 6.dp)
            )
        }
        MyCustomProgressBar(padding_top = 0, maxHeight = 1, progress = 0f)
    }

}

@Composable
fun UIDeleteBudgetButton(onDeleteClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDeleteClick() }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "Delete Budget",
                tint = Color(0xFFC4311D),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Delete Budget",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFC4311D)
            )
        }
    }


}