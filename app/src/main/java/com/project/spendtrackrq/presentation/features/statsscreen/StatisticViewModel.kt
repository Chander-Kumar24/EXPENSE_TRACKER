package com.project.spendtrackrq.presentation.features.statsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spendtrackrq.data.local.dao.TransactionDAO
import com.project.spendtrackrq.data.local.dto.TransactionInfo
import com.project.spendtrackrq.data.model.TopCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class TimeRange {
    CURRENT_MONTH,
    LAST_SIX_MONTHS,
    ALL_TIME
}

sealed class DateRange {
    object CurrentMonth : DateRange()
    object LastMonth : DateRange()
    object LastSixMonths : DateRange()
    object AllTime : DateRange()
}

enum class TransactionType {
    INCOME,
    EXPENSE
}

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val transactionDAO: TransactionDAO,
) : ViewModel() {

    private val _selectedTimeRange = MutableStateFlow(TimeRange.CURRENT_MONTH)
    private val _selectedTransactionType = MutableStateFlow(TransactionType.EXPENSE)
    private val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
    private val calendar = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }
    private val previousMonth =
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)

    val currentMonthIncome = transactionDAO.getIncomeByMonth(currentMonth)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val previousMonthIncome = transactionDAO.getIncomeByMonth(previousMonth)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val currentMonthExpense = transactionDAO.getExpenseByMonth(currentMonth)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val previousMonthExpense = transactionDAO.getExpenseByMonth(previousMonth)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    // Last 6 months data
    val lastSixMonthsIncome = transactionDAO.getIncomeLastSixMonths()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val lastSixMonthsExpense = transactionDAO.getExpenseLastSixMonths()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // All time data
    val allTimeIncome = transactionDAO.getTotalIncome()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val allTimeExpense = transactionDAO.getTotalExpense()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    // Calculate percentage change
    val incomePercentageChange = combine(
        currentMonthIncome,
        previousMonthIncome
    ) { current, previous ->
        if (previous == 0f) 0f else ((current - previous) / previous) * 100
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val expensePercentageChange = combine(
        currentMonthExpense,
        previousMonthExpense
    ) { current, previous ->
        if (previous == 0f) 0f else ((current - previous) / previous) * 100
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)


    val chartData = combine(
        _selectedTimeRange,
        _selectedTransactionType,
        currentMonthIncome,
        currentMonthExpense,
        lastSixMonthsIncome,
        lastSixMonthsExpense,
        allTimeIncome,
        allTimeExpense
    ) { values ->
        val timeRange = values[0] as TimeRange
        val type = values[1] as TransactionType
        val monthIncome = values[2] as Float
        val monthExpense = values[3] as Float
        val sixMonthsIncome = values[4] as Map<*, *>
        val sixMonthsExpense = values[5] as Map<*, *>
        val totalIncome = values[6] as Float
        val totalExpense = values[7] as Float

        when (timeRange) {
            TimeRange.CURRENT_MONTH -> {
                val amount = if (type == TransactionType.INCOME) monthIncome else monthExpense
                mapOf(currentMonth to amount)
            }

            TimeRange.LAST_SIX_MONTHS -> {
                if (type == TransactionType.INCOME) sixMonthsIncome else sixMonthsExpense
            }

            TimeRange.ALL_TIME -> {
                val total = if (type == TransactionType.INCOME) totalIncome else totalExpense
                mapOf("All Time" to total)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val topCategories: Flow<List<TopCategory>> = transactionDAO.getTopCategoriesByAmount()
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    val categoryWithColors: Flow<Map<String, Long>> = topCategories.map { categories ->
        val colorList = listOf(
            0xFFFF6B6B,  // Red
            0xFF4ECDC4,  // Teal
            0xFF45B7D1,  // Blue
            0xFF96CEB4,  // Green
            0xFFFFEEAD,  // Yellow
            0xFFFF9F1C,  // Orange
            0xFF9B5DE5,  // Purple
            0xFFF15BB5   // Pink
        )

        categories.mapIndexed { index, category ->
            category.categoryName to colorList[index % colorList.size]
        }.toMap()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyMap()
    )

    private val _lineChartData =
        transactionDAO.getTransactionInfoChart(_selectedTransactionType.value)
            .map { transactionInfos ->
                val dateFormat = SimpleDateFormat("dd/MMM", Locale.getDefault())
                transactionInfos.groupBy { transactionInfo ->
                    dateFormat.format(Date(transactionInfo.timestamp))
                }.mapValues { (_, transactions) ->
                    transactions.sumOf { it.amount.toDouble() }.toFloat()
                }.toList()
                    .sortedBy { (date, _) ->
                        dateFormat.parse(date)?.time ?: 0L
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    //val lineChart = _lineChartData

    private fun getLineChartData(transactionType: TransactionType) =
        transactionDAO.getTransactionInfoChart(transactionType)
            .map { transactionInfos ->
                val dateFormat = SimpleDateFormat("dd/MMM", Locale.getDefault())
                transactionInfos.groupBy { transactionInfo ->
                    dateFormat.format(Date(transactionInfo.timestamp))
                }.mapValues { (_, transactions) ->
                    transactions.sumOf { it.amount.toDouble() }.toFloat()
                }.toList()
                    .sortedBy { (date, _) ->
                        dateFormat.parse(date)?.time ?: 0L
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    private val _lineChartType = MutableStateFlow(TransactionType.EXPENSE)
    val lineChartType = _lineChartType.asStateFlow()

    /* val lineChart = _lineChartType.flatMapLatest { type ->
         when (type) {
             TransactionType.INCOME -> getLineChartData(TransactionType.INCOME)
             TransactionType.EXPENSE -> getLineChartData(TransactionType.EXPENSE)
         }
     }.stateIn(
         viewModelScope,
         SharingStarted.WhileSubscribed(5000),
         emptyList()
     )*/

    fun setLineChartType(type: TransactionType) {
        _lineChartType.value = type
    }

    private fun getChartDataByDateRange(
        transactionType: TransactionType,
        dateRange: DateRange,
    ): Flow<List<TransactionInfo>> {
        return when (dateRange) {
            is DateRange.CurrentMonth -> {
                val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
                transactionDAO.getTransactionInfoByMonth(transactionType, currentMonth)
            }

            is DateRange.LastMonth -> {
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                }
                val lastMonth =
                    SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
                transactionDAO.getTransactionInfoByMonth(transactionType, lastMonth)
            }

            is DateRange.LastSixMonths -> {
                transactionDAO.getTransactionInfoLastSixMonths(transactionType)
            }

            is DateRange.AllTime -> {
                transactionDAO.getTransactionInfoChart(transactionType)
            }
        }
    }

    private val _selectedDateRange = MutableStateFlow<DateRange>(DateRange.CurrentMonth)
    val selectedDateRange = _selectedDateRange.asStateFlow()
    fun setDateRange(dateRange: DateRange) {
        _selectedDateRange.value = dateRange
    }

    val lineChart = combine(
        _lineChartType,
        _selectedDateRange
    ) { type, dateRange ->
        getChartDataByDateRange(type, dateRange).map { transactions ->
            val dateFormat = when (dateRange) {
                is DateRange.CurrentMonth, is DateRange.LastMonth ->
                    SimpleDateFormat("dd/MM", Locale.getDefault())

                is DateRange.LastSixMonths ->
                    SimpleDateFormat("MMM yyyy", Locale.getDefault())

                is DateRange.AllTime ->
                    SimpleDateFormat("MMM yyyy", Locale.getDefault())
            }

            // Group transactions by their formatted date
            val grouped = transactions.groupBy { transaction ->
                dateFormat.format(Date(transaction.timestamp))
            }.mapValues { (_, trans) ->
                trans.sumOf { it.amount.toDouble() }.toFloat()
            }
            when (dateRange) {
                is DateRange.LastSixMonths, is DateRange.AllTime -> {
                    val calendar = Calendar.getInstance()
                    val monthsToShow =
                        if (dateRange is DateRange.LastSixMonths) 6 else 12 * 5 // 6 months or 5 years

                    val allMonths = List(monthsToShow) { i ->
                        calendar.time = Date()
                        calendar.add(Calendar.MONTH, -i)
                        dateFormat.format(calendar.time) to 0f
                    }.reversed()
                    allMonths.map { (month, _) ->
                        month to (grouped[month] ?: 0f)
                    }
                }

                else -> {
                    grouped.toList().sortedBy { (date, _) ->
                        try {
                            dateFormat.parse(date)?.time ?: 0L
                        } catch (e: Exception) {
                            0L
                        }
                    }
                }
            }
        }
    }.flattenMerge().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

}