package com.project.spendtrackrq.presentation.features.budget.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity
import com.project.spendtrackrq.presentation.common.UICommonButton
import com.project.spendtrackrq.presentation.theme.greenColor
import com.project.spendtrackrq.presentation.theme.redColor

@Composable
fun UICategoryAddBudgetDialog(
    categories: List<CategoryEntity>,
    selectedCategory: CategoryEntity?,
    amount: String,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (CategoryEntity) -> Unit,
    isEditing: Boolean = false,
    onDelete: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (!isEditing) "Add Category Budget" else "Edit Category Budget",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                }
            }

            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Amount") },
                placeholder = { Text("0.00") },
                leadingIcon = { Text("$") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(Modifier.height(24.dp))

            if (!isEditing) {


                Text(
                    "Select Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    maxItemsInEachRow = 3,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = category == selectedCategory

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) Color(0xFF2F7E79) else Color.Gray.copy(alpha = 0.15f),
                            contentColor = if (isSelected) Color.White else Color.DarkGray,
                            modifier = Modifier.clickable {
                                onCategoryChange(category)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(category.imgId),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(category.categoryName, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }





            UICommonButton(
                "CONFIRM BUDGET",
                textSize = 18.sp,
                onConfirm,
                gradientColors = greenColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            if (isEditing) {
                Spacer(Modifier.height(16.dp))
                UICommonButton(
                    "DELETE BUDGET",
                    textSize = 16.sp,
                    onDelete,
                    gradientColors = redColor,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(48.dp)
                )
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CustomDialogContentPreviews() {
//    UICategoryAddBudgetDialog()
//}
