package com.project.spendtrackrq.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.local.entities.transaction.TransactionEntity
import com.project.spendtrackrq.data.models.enums.TransactionType
import com.project.spendtrackrq.data.models.topbar.TopAppBarDTO
import com.project.spendtrackrq.data.models.transaction.TransactionDTO
import com.project.spendtrackrq.data.models.transaction.TransactionDetailItem
import com.project.spendtrackrq.data.models.transaction.TransactionFilter
import com.project.spendtrackrq.presentation.common.components.transactionhistory.UIDialogBoxDeleteTxn
import com.project.spendtrackrq.presentation.features.banktxn.components.transaction.UITxnBottomModalSheet
import com.project.spendtrackrq.presentation.features.expense.ExpenseViewModel
import com.project.spendtrackrq.presentation.theme.expenseColor
import com.project.spendtrackrq.presentation.theme.incomeColor
import com.project.spendtrackrq.utils.AllPreviews
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AllPreviews
@Composable
fun showTxn()
{
    //UITransactionHistory(navController = NavHostController(LocalContext.current))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UITransactionHistory(
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController,
    category: String?
) {
    val transactions by expenseViewModel.transactionState.collectAsState()
    val filtered by expenseViewModel.filteredTransactionState.collectAsState()

    var selectedItems by remember { mutableStateOf<Set<TransactionDTO>>(emptySet()) }
    val isSelectionMode = selectedItems.isNotEmpty()
    val isSingleSelection = selectedItems.size == 1
    var deleteDialog by remember { mutableStateOf(false) }

    var filterState by remember { mutableStateOf(TransactionFilter.ALL) }

    val screenTitle = if (category == null || category == "all") {
        "All Transactions"
    } else {
        category
    }
    val displayList = if (category == null || category == "all") {
        transactions
    } else {
        filtered
    }

    val filterText = when (filterState) {
        TransactionFilter.ALL -> "All Transactions"
        TransactionFilter.INCOME -> "Top Income Transactions"
        TransactionFilter.EXPENSE -> "Top Expense Transactions"
    }

    val filteredList = when (filterState) {
        TransactionFilter.ALL -> displayList.sortedByDescending { it.formattedDate }
        TransactionFilter.INCOME -> displayList.sortedWith(
            compareByDescending<TransactionEntity> {
                it.transactionType == TransactionType.INCOME
            }.thenByDescending { it.formattedDate }
        )

        TransactionFilter.EXPENSE -> displayList.sortedWith(
            compareByDescending<TransactionEntity> {
                it.transactionType == TransactionType.EXPENSE
            }.thenByDescending { it.formattedDate }
        )
    }

    val deleteAction = TopAppBarDTO(
        R.drawable.delete,
        contentDescription = "Delete",
        onClick = {
            deleteDialog = true
        },
        size = 24.dp
    )
    val editAction = TopAppBarDTO(
        R.drawable.edit,
        contentDescription = "Edit",
        onClick = {
            selectedItems.firstOrNull()?.let { transaction ->
                navController.navigate("expenseAdder/${transaction.id}")
            }
        },
        size = 18.dp
    )

    val actions = buildList {
        if (isSingleSelection) {
            add(editAction)
        }
        if (isSelectionMode) {
            add(deleteAction)
        }
    }
    if (deleteDialog) {
        UIDialogBoxDeleteTxn(
            onDismiss = {
                deleteDialog = false
                selectedItems = emptySet()
            },
            onDelete = {
                val transactionIds = selectedItems.map { it.id }
                if (transactionIds.isNotEmpty()) {
                    expenseViewModel.deleteTransaction(transactionIds)
                }
                selectedItems = emptySet()
                deleteDialog = false
            }
        )
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            UITabRow(
                navController = navController,
                text = screenTitle,
                actions = actions,
                isVisible = isSelectionMode
            )
        }
    )
    { innerpadding ->
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .background(MaterialTheme.colorScheme.background)

        )
        {
            //Add Category
            //TODO: All Transactions/Expense/Income Transactions..Maybe animate the icon when clicked
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 36.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = filterText,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color(0xFF222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,

                    )

                Spacer(Modifier.weight(1f))

                IconButton(onClick = {
                    filterState = when (filterState) {
                        TransactionFilter.ALL -> {
                            TransactionFilter.INCOME
                        }

                        TransactionFilter.INCOME -> {
                            TransactionFilter.EXPENSE
                        }

                        TransactionFilter.EXPENSE -> {
                            TransactionFilter.ALL
                        }
                    }
                })
                {
                    Icon(
                        painter = painterResource(R.drawable.bx_sort_1),
                        contentDescription = "Filter",
                        tint=Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // we dont want it run everytime it recompses taht is it changes
            //display list changes when we click kon filter

            val groupedTransactions = remember(filteredList) {
                filteredList.groupBy { transaction ->
                    getDateHeader(transaction.formattedDate)
                }.toList().sortedByDescending { (header, _) ->
                    when (header) {
                        "Today" -> 2
                        "Yesterday" -> 1
                        else -> 0
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                groupedTransactions.forEach { (header, transactionGroup) ->
                    item {
                        UICardDateHeader(title = header)
                    }
                    items(transactionGroup) { transaction ->
                        val transactionModel = TransactionDTO(
                            id = transaction.id,
                            merchantName = transaction.merchant,
                            categoryImage = transaction.categoryImage,
                            date = transaction.formattedDate,
                            amount = transaction.amount,
                            categoryName = transaction.categoryName,
                            transactionId = transaction.transactionId,
                            transactionType = transaction.transactionType,
                            paymentMethod = null
                        )
                        val anyCardSelected =
                            selectedItems.any { it.id == transaction.id }
                        UITxnCard(
                            transaction = transactionModel,
                            isSelected = anyCardSelected,
                            isSelectionMode = isSelectionMode,
                            onSelectionChange = { selected ->
                                selectedItems = if (selected) {
                                    selectedItems + transactionModel
                                } else {
                                    selectedItems - transactionModel
                                }
                            },
                        )
                    }
                }
            }
        }
    }

}

fun getDateHeader(dateString: String): String {
    // Input format: EEE, dd MMM yyyy (e.g., "Mon, 16 Jan 2023")
    // Output format: MMM dd, yyyy (e.g., "Jan 16, 2023")

    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    return try {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val date = inputFormat.parse(dateString) ?: return outputFormat.format(
            inputFormat.parse(
                dateString
            ) ?: Date()
        )
        val cal = Calendar.getInstance().apply { time = date }

        when {
            cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Today"

            cal.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                    cal.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> "Yesterday"

            else -> outputFormat.format(date)
        }
    } catch (e: Exception) {
        // In case of any parsing error, return the original string or a formatted version if possible
        try {
            outputFormat.format(inputFormat.parse(dateString) ?: Date())
        } catch (e: Exception) {
            dateString
        }
    }
}

@Composable
fun UITxnCard(
    transaction: TransactionDTO,
    isCredit: Boolean = false,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    isClickable: Boolean = true,
    onSelectionChange: (Boolean) -> Unit = {},
    )
{
    //var isLongPressed by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) }

    val listItem = listOf(TransactionDetailItem("Payment Method", transaction.paymentMethod?.name ?: "-", null),
        TransactionDetailItem("To", transaction.merchantName, null),
        TransactionDetailItem("Type", transaction.transactionType.name, null),
        TransactionDetailItem("Category", transaction.categoryName ?: "-",null),
        TransactionDetailItem("Date", transaction.date, null),
        TransactionDetailItem(
            "Transaction ID",
            if (transaction.transactionId.isNullOrBlank()) "-" else transaction.transactionId,
            if (!transaction.transactionId.isNullOrBlank()) R.drawable.copy_simple_fill_1 else null
        )   )

    val fixedItemList = listOf(
        TransactionDetailItem(if (isCredit) "Credit" else "Debit", "$${transaction.amount}", null),
       // TransactionDetailItem("Balance", "$893.2", null)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF29756F) else MaterialTheme.colorScheme.background,
        animationSpec = tween(durationMillis = 150)
    )
    UICommonTxnCard(
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = {
                    if (isClickable) {
                        if (isSelectionMode) {
                            onSelectionChange(!isSelected)
                        } else {
                            showInfoSheet = true
                        }
                    }
                },
                onLongClick = {
                    if (!isSelectionMode && !isSelected) {
                        onSelectionChange(true)
                    }
                }
            )
            .then(
                if (isSelected) {
                    Modifier.dropShadow(
                        RoundedCornerShape(16.dp),
                        shadow = Shadow(
                            radius = 10.dp,
                            spread = 3.dp,
                            color = Color(0x3529756F),
                            offset = DpOffset(x = 0.dp, 12.dp)
                        )
                    )
                } else {
                    Modifier
                }
            )
            .fillMaxWidth()
            .height(80.dp),
        cardBgColor = backgroundColor, //surface
        elevation = if (isSelected) CardDefaults.cardElevation(defaultElevation = 64.dp) else
            CardDefaults.cardElevation(defaultElevation = 0.dp),

        )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(
                6.dp,
                Alignment.CenterVertically
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = transaction.categoryImage),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = transaction.merchantName,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF000000)
                    )

                    Text(
                        text = transaction.date,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF000000)
                    )
                }
                Text(
                    text = (if (transaction.transactionType == TransactionType.INCOME) "+ $" else "- $") + transaction.amount,
                    color = if (isSelected) Color.White else if (transaction.transactionType == TransactionType.EXPENSE) expenseColor else incomeColor,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }

    if (isClickable && showInfoSheet && !isSelectionMode)
    {
        UITxnBottomModalSheet(
            listItem ,
            fixedItemList,
            onDismiss = {showInfoSheet = false}
        )
    }
}


@Composable
fun UICardDateHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666),
            fontFamily = FontFamily(Font(R.font.inter_medium))
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp),
            thickness = 1.dp
        )
    }
}