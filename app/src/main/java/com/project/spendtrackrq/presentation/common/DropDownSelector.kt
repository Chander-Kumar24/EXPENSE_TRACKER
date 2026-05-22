package com.project.spendtrackrq.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.spendtrackrq.data.local.entities.category.CategoryEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    categoryOptions: List<CategoryEntity>,
    currentOption: CategoryEntity? = null,
    onCategoryChange: (CategoryEntity) -> Unit,
) {
    // val context = LocalContext.current


    var expanded by remember { mutableStateOf(false) }
    //var selected by remember { mutableStateOf<CategoryEntity?>(null) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },// ← Handles the toggle
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    1.dp, Color(0xFFDDDDDD),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(Color.White, shape = RoundedCornerShape(10.dp))
        ) {
            // Use a single Row as your custom "field"
            Row(
                modifier = Modifier
                    .menuAnchor()// ← Makes it clickable + triggers onExpandedChange
                    // No .clickable needed!
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                if (currentOption != null) {
                    Image(
                        painter = painterResource(currentOption.imgId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = currentOption?.categoryName ?: "Select a category",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = if (currentOption == null) Color.Gray else MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.weight(1f))

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }

        // Dropdown list
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryOptions.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.categoryName) },
                    leadingIcon = {
                        Image(
                            painter = painterResource(item.imgId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(MaterialTheme.shapes.small),
                            contentScale = ContentScale.Fit
                        )
                    },
                    onClick = {
                        onCategoryChange(item)
                        expanded = false
                        //Toast.makeText(context, item.categoryName, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}