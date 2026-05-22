package com.project.spendtrackrq.presentation.features.expense

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.data.models.dropdown.DropdownDTO
import com.project.spendtrackrq.data.models.enums.TransactionType
import com.project.spendtrackrq.presentation.common.SimpleDropdown
import com.project.spendtrackrq.utils.AllPreviews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AllPreviews
@Composable()
fun ShowExpenseScreen() {
    //UIExpenseAdder(rememberNavController())
}

@Composable
fun UIExpenseAdder(
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController? = null,
    transactionId: Int = -1
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { inner ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (banner, card, backIcon, doneIcon, titleIcon) = createRefs()

                Image(
                    painter = painterResource(R.drawable.defaultupbanner),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                        .constrainAs(banner)
                        {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.FillBounds
                )

                createHorizontalChain(
                    backIcon,
                    titleIcon,
                    doneIcon,
                    chainStyle = ChainStyle.SpreadInside
                )

                IconButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .constrainAs(backIcon) {

                            top.linkTo(parent.top, margin = 16.dp)
                        }
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }


                Text(
                    text = if (transactionId != -1) "Edit Expense" else "Add Expense",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .constrainAs(titleIcon) {

                            top.linkTo(backIcon.top)
                            bottom.linkTo(backIcon.bottom)
                        },
                    textAlign = TextAlign.Center,
                )


                IconButton(
                    onClick = {
                        if (transactionId == -1) {
                            expenseViewModel.addTransaction()
                        } else {
                            expenseViewModel.updateTransaction(transactionId)
                        }

                    },
                    modifier = Modifier
                        .constrainAs(doneIcon) {

                            top.linkTo(backIcon.top)
                            bottom.linkTo(backIcon.bottom)
                        }
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.done),
                        contentDescription = "Done",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                UIExpenseAddCard(expenseViewModel, Modifier.constrainAs(card) {
                    top.linkTo(banner.bottom, margin = (-106.dp))
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.wrapContent
                }, transactionId ?: -1)

            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


//TODO: Make the dropdown and Date picker expendale
@Composable
fun UIExpenseAddCard(
    expenseViewModel: ExpenseViewModel,
    modifier: Modifier = Modifier,
    transactionId: Int = -1
) {
    val _expenseViewModel by expenseViewModel.transaction.collectAsState()
    val getCategory by expenseViewModel.getCategories.collectAsState()

    val getCurrentTransaction by expenseViewModel.getTransactionById(transactionId)
        .collectAsState(initial = null)
    LaunchedEffect(getCurrentTransaction)
    {
        if (getCurrentTransaction != null) {
            expenseViewModel.onNameChange(getCurrentTransaction?.merchant ?: "")
            expenseViewModel.onCategoryChange(
                CategoryEntity(
                    getCurrentTransaction?.categoryId ?: 0,
                    getCurrentTransaction?.categoryName ?: "",
                    getCurrentTransaction?.categoryImage ?: 0
                )
            )
            expenseViewModel.onAmountChange(getCurrentTransaction?.amount ?: 0.0f)
            expenseViewModel.onDateChange(getCurrentTransaction?.formattedDate ?: "")
            expenseViewModel.onTransactionIdChange(getCurrentTransaction?.transactionId ?: "")
            expenseViewModel.onTabChange(
                getCurrentTransaction?.transactionType ?: TransactionType.EXPENSE
            )
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 24.dp)
        ) {

            TabRowCard(expenseViewModel)
            Spacer(Modifier.height(32.dp))

            Text(
                text = "NAME",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))

            UIAddName(
                _expenseViewModel.merchant,
                { expenseViewModel.onNameChange(it) },
                "Example - BURGER KING"
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

            SimpleDropdown(
                categoryOptions = getCategory,
                _expenseViewModel.selectedCategory,
                onCategoryChange = { selectedCategory ->
                    // When the dropdown reports a change, we tell the ViewModel.
                    expenseViewModel.onCategoryChange(selectedCategory)
                })

            Text(
                text = "AMOUNT",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            PriceWithClearButton(_expenseViewModel.amount, { expenseViewModel.onAmountChange(it) })

            Text(
                text = "DATE",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp),

                )

            Spacer(modifier = Modifier.height(18.dp))
            DatePickerField(
                _expenseViewModel.date,
                { expenseViewModel.onDateChange(it) },
            )

            /*Text(
                text = "INVOICE",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            InvoiceAdder(clickFunction = {}) //TODO: Only let upload either image or pdf*/
            Text(
                text = "TRANSACTIONS ID",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))

            UIAddName(
                _expenseViewModel.transactionId,
                { expenseViewModel.onTransactionIdChange(it) },
                placeholder = "Example - ABCDEF987654321")
            Spacer(modifier = Modifier.height(18.dp))

        }
    }
}

@Composable
fun UIAddName(text: String, onTextChange: (String) -> Unit, placeholder: String = "Example") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp))
            .background(Color.White, shape = RoundedCornerShape(10.dp))
    ) {

        TextField(
            value = text,
            onValueChange = { onTextChange(it) },
            modifier = Modifier.fillMaxSize(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF666666).copy(alpha = 0.28f),
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color(0xFF666666),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun TabRowCard(expenseViewModel: ExpenseViewModel) {
    val tabList = listOf(TransactionType.EXPENSE, TransactionType.INCOME)

    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedIndex = tabList.indexOf(selectedType)
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp)),
        containerColor = Color(0xFFF4F6F6),
        contentColor = Color.Black,
        indicator = {}

    ) {
        tabList.forEachIndexed { index, title ->
            Tab(
                modifier = if (selectedIndex == index)
                    Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White) else
                    Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF4F6F6)),
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    selectedType = tabList[index]
                    expenseViewModel.onTabChange(selectedType)

                },
                text = {
                    Text(
                        text = title.name,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF666666)
                    )
                }
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceWithClearButton(
    amt: Float,
    amtChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    var inputText by remember { mutableStateOf(amt.toString()) }

    LaunchedEffect(amt) {
        if (!isFocused) {
            inputText = if (amt == 0f) "" else "%.2f".format(amt)
        }
    }

    val displayText = remember(amt, isFocused) {
        when {
            isFocused -> inputText
            amt == 0f -> "0.00"
            else -> "%.2f".format(amt)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp))
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = displayText,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() || it == '.' }

                // Handle decimal places logic
                val parts = filtered.split(".")
                when {
                    parts.size == 1 -> {
                        // No decimal point or only integer part
                        inputText = filtered
                        val amountValue = filtered.toFloatOrNull() ?: 0f
                        amtChanged(amountValue)
                    }
                    parts.size == 2 -> {
                        // Has decimal part - limit to 2 decimal places
                        val decimalPart = if (parts[1].length > 2) {
                            parts[1].take(2)
                        } else {
                            parts[1]
                        }
                        inputText = "${parts[0]}.$decimalPart"
                        val amountValue = "${parts[0]}.$decimalPart".toFloatOrNull() ?: 0f
                        amtChanged(amountValue)
                    }
                }
            },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    val wasFocused = isFocused
                    isFocused = focusState.isFocused

                    if (!wasFocused && focusState.isFocused) {
                        // Just gained focus - prepare for editing
                        inputText = if (amt == 0f) "" else amt.toString()
                    } else if (wasFocused && !focusState.isFocused) {
                        // Just lost focus - format the value
                        val value = inputText.toFloatOrNull() ?: 0f
                        inputText = if (value == 0f) "" else "%.2f".format(value)
                        amtChanged(value)
                        focusManager.clearFocus()
                    }
                },
            singleLine = true,
            placeholder = {
                Text("0.00", color = Color(0xFF999999))
            },
            textStyle = LocalTextStyle.current.copy(
                color = Color(0xFF666666),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontWeight = FontWeight.SemiBold
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            prefix = {
                Text("$ ", color = Color(0xFF666666))
            }
        )

        // Show Clear button only when there's a non-zero value
        if (amt != 0f) {
            Text(
                text = "Clear",
                color = Color(0xFF666666),
                modifier = Modifier
                    .clickable {
                        inputText = ""
                        amtChanged(0f)  // Notify parent that amount is cleared
                        focusManager.clearFocus()
                    }
                    .padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    date: String,
    onDateChange: (String) -> Unit,
    initialDate: Date = Date(),
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()) }

    val initialDateText = remember(initialDate) {
        formatter.format(initialDate)
    }

    // Initialize with current date if empty
    LaunchedEffect(Unit) {
        if (date.isEmpty()) {
            onDateChange(initialDateText)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp))
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { showDatePicker = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the selected date
        Text(
            text = date.ifEmpty { initialDateText },
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            color = if (date.isEmpty()) Color(0xFF666666) else Color.Black,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontWeight = if (date.isNotEmpty()) FontWeight.SemiBold else FontWeight.Normal
        )

        // Calendar icon
        Icon(
            painter = painterResource(R.drawable.calendar),
            contentDescription = "Select Date",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(24.dp),
            tint = Color.Unspecified
        )
    }

    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = try {
                formatter.parse(date)?.time ?: System.currentTimeMillis()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { millis ->
                            val selectedDate = Date(millis)
                            onDateChange(formatter.format(selectedDate))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = state,
                title = { Text("Select Date") },
                headline = { Text("Choose a date") },
                showModeToggle = true
            )
        }
    }
}


@Composable
fun InvoiceAdder(contentText: String = "Add Invoice", clickFunction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { clickFunction() }
            .drawBehind {

                val pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(20f, 10f), // dash length, gap length
                    phase = 0f
                )

                // Draw dashed border
                drawRoundRect(
                    color = Color.Gray,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = pathEffect
                    ),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus_circle),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = contentText,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666),
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableExposedDropdownMenuSample(
    dropdownOptions: List<DropdownDTO>,
    modifier: Modifier = Modifier,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    defaultIcon: Int? = null,
    placeholder: String = ""
) {

    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    //var selectedOption by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val optionIcons: Map<String, Int> = dropdownOptions.associateBy({ it.category }, { it.imgId })


    val allOptions = optionIcons.keys.toList()


    val filteredOptions = if (query.isBlank()) {
        allOptions
    } else {
        allOptions.filter {
            it.contains(query, ignoreCase = true)
        }
    }
    ExposedDropdownMenuBox(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 2.dp),
        expanded = expanded,
        onExpandedChange = { newExpanded ->
            expanded = newExpanded
            if (newExpanded && selectedOption.isNotEmpty() && !isEditing) {

                isEditing = true
                query = ""
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp))
                .background(Color.White, shape = RoundedCornerShape(10.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            if (selectedOption.isNotEmpty()) {

                                optionIcons[selectedOption] ?: (defaultIcon
                                    ?: R.drawable.food_active)
                            } else {

                                defaultIcon ?: R.drawable.food_active
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }


                Box(
                    modifier = Modifier
                        .menuAnchor()
                        .weight(1f)
                        .height(56.dp)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    if (query.isEmpty() && selectedOption.isEmpty()) {
                        Text(
                            placeholder,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = Color(0x28666666),
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Italic,
                            fontSize = 16.sp
                        )
                    }


                    BasicTextField(
                        value = if (isEditing) query else selectedOption,
                        onValueChange = { newValue ->
                            if (!isEditing) {

                                isEditing = true
                            }
                            query = newValue
                            expanded = true
//                            if (newValue.isNotEmpty()) {
//                               // selectedOption = ""
//                                //onOptionSelected("")
//                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        readOnly = !isEditing && selectedOption.isNotEmpty(),
                        decorationBox = { innerTextField ->

                            Box {
                                innerTextField()
                            }
                        }
                    )
                }


                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }


        ExposedDropdownMenu(
            expanded = expanded && filteredOptions.isNotEmpty(),
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()

                if (selectedOption.isNotEmpty()) {
                    isEditing = false
                    query = ""
                }
            }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    leadingIcon = {
                        Image(
                            painter = painterResource(
                                optionIcons[option] ?: R.drawable.food_active
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.Fit
                        )
                    },
                    text = {
                        Text(
                            option,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        //selectedOption = option
                        query = ""
                        expanded = false
                        isEditing = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }

}

