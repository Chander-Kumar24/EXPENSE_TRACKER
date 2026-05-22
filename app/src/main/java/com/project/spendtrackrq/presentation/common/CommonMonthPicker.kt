package com.project.spendtrackrq.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.project.spendtrackrq.R


@Composable
fun MainMethod() {
    // Colors
    val salmonColor = Color(0xFFEF8879)
    val cardBgColor = Color.White
    val textColor = Color(0xFF2C2C2C)

    // Data Setup
    var currentYear by remember { mutableIntStateOf(2021) }
    var selectedMonth by remember { mutableStateOf("July") }

    val months = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    // Main Card Content
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Reduced padding to give grid more space
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Header (Arrows and Year) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { currentYear-- },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Year",
                        tint = Color.Gray
                    )
                }

                Text(
                    text = "$currentYear",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                IconButton(
                    onClick = { currentYear++ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Year",
                        tint = Color.Gray
                    )
                }
            }

            // --- Month Grid ---
            // Changed to 3 Columns so "September" fits on one line
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(months) { month ->
                    val isSelected = month == selectedMonth

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(40.dp)
                            .background(
                                color = if (isSelected) salmonColor else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedMonth = month }
                    ) {
                        Text(
                            text = month,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else textColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1, // Forces single line
                            softWrap = false, // Prevents wrapping
                            overflow = TextOverflow.Visible // Ensures text stays visible
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogContentPreviews() {
    // UIMonthPicker()
}

@Composable
fun UIMonthPicker(
    selectedMonth: Int = 0, // 1-12, 0 means no selection
    selectedYear: Int = 0,
    onMonthSelected: (month: Int, year: Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1 // 1-12

    // Display the selected month and year, or a default text if not selected
    val displayText = if (selectedMonth in 1..12 && selectedYear > 0) {
        "${months[selectedMonth - 1]} $selectedYear"
    } else {
        "Select Month"
    }

    // Show the month picker button with original design
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp))
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display selected month and year or default text
        Text(
            text = displayText,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            color = if (selectedMonth == 0) Color(0xFF666666) else Color.Black,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontWeight = if (selectedMonth > 0) FontWeight.SemiBold else FontWeight.Normal
        )

        // Calendar icon
        Icon(
            painter = painterResource(R.drawable.calendar),
            contentDescription = "Select Month",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(24.dp),
            tint = Color.Unspecified
        )
    }

    // Month picker dialog - keeping the original MainMethod design
    if (showDialog) {
        var year by remember { mutableIntStateOf(if (selectedYear > 0) selectedYear else currentYear) }

        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Year selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Only allow going back to the current year
                        val canGoToPreviousYear = year > currentYear
                        IconButton(
                            onClick = { if (canGoToPreviousYear) year-- },
                            enabled = canGoToPreviousYear
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Year",
                                tint = if (canGoToPreviousYear) Color.Black else Color.LightGray
                            )
                        }

                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        IconButton(
                            onClick = { year++ }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Year",
                                tint = Color.Black
                            )
                        }
                    }

                    // Month grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        items(months) { month ->
                            val monthIndex = months.indexOf(month) + 1
                            val isPastYear = year < currentYear
                            val isCurrentYear = year == currentYear
                            val isPastMonth =
                                isPastYear || (isCurrentYear && monthIndex < currentMonth)
                            val isSelected = monthIndex == selectedMonth && year == selectedYear

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        color = if (isSelected) Color(0xFF3F8782) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable(
                                        enabled = !isPastMonth,
                                        onClick = {
                                            onMonthSelected(monthIndex, year)
                                            showDialog = false
                                        }
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = month.take(3),
                                    color = when {
                                        isSelected -> Color.White
                                        isPastMonth -> Color.Gray
                                        else -> Color.Black
                                    },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Cancel button
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F8782))
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                }
            }
        }
    }
}