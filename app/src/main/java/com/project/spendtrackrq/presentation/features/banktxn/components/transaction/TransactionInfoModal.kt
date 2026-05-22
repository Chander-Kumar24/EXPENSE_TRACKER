package com.project.spendtrackrq.presentation.features.banktxn.components.transaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.models.transaction.TransactionDetailItem
import com.project.spendtrackrq.presentation.common.UICommonModalSheet
import com.project.spendtrackrq.utils.AllPreviews


@AllPreviews
@Composable
fun ShowTxnBankModal()
{
    UITxnBottomModalSheet(itemList = emptyList(), emptyList(), false)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UITxnBottomModalSheet(
    itemList: List<TransactionDetailItem>,
    fixedItemList: List<TransactionDetailItem>,
    isWarning: Boolean = false,
                          onDismiss: () -> Unit = {})
{
    UICommonModalSheet(fraction = 0.7f,onDismiss=onDismiss)
    {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,)
        {
            /*if(!isWarning)
            {
                Image(
                    painter = painterResource(R.drawable.greenvector),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp))
            } else {
                Image(
                    painter = painterResource(R.drawable.info),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp))
            }

            // Title Text
            Text(
                text = if (isWarning) "Potential Duplicates"
                else if (isDebit) "Debited Successfully"
                else "Credited Successfully",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if(!isWarning) Color(0xFF438883) else Color(0xFFFF0202),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                textAlign = TextAlign.Center
            )
            Text(
                text = when {
                    isWarning -> "Transaction already exists.\nReview it."
                    isDebit -> "To 4897695162091"
                    else -> "From 4897695162091"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )*/

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp))
            {
                UIHeaderExpendableModal(R.drawable.chevron, "Transaction Details")
                UITransactionDetails(itemList, fixedItemList, isWarning)
            }
        }
    }
}

@Composable
fun UIHeaderExpendableModal(imgResId: Int, headerStr: String = "")
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            text = headerStr,
            color = Color(0xFF222222),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(imgResId),
                contentDescription = null
            )
        }
    }
}

@Composable
fun UITransactionDetails(
    txnUIList: List<TransactionDetailItem>,
    fixedList: List<TransactionDetailItem>,
    isWarning: Boolean = false
)
{
    // UIRowMaker("Payment Method", "UPI", null)
    @Composable
    fun UIRowMaker(item: TransactionDetailItem)
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF666666)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color(0xFF000000),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = if (item.iconRes != null) 8.dp else 0.dp)
                )
                if (item.iconRes != null)
                {
                    IconButton(onClick = {})
                    {
                        Icon(
                            painter = painterResource(item.iconRes),
                            contentDescription = "Copy",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly)
    {
        // Transaction Details
        Column {
            txnUIList.forEach { item ->
                UIRowMaker(item)
            }


            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            fixedList.forEach { item ->
                UIRowMaker(item)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .width(330.dp)
                    .height(60.dp),
                border = BorderStroke(1.dp, Color(0xFF408782)),
            ) {
                Text(
                    text = if(!isWarning) "Share Receipt" else "Overwrite",
                    color = Color(0xFF438883),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Bold
                )
            }

            if(isWarning) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .width(330.dp)
                        .height(60.dp),
                    border = BorderStroke(1.dp, Color(0xFF000000)),
                ) {
                    Text(
                        text = "Add as New",
                        color = Color(0xFF000000),
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .width(330.dp)
                        .height(60.dp),
                    border = BorderStroke(1.dp, Color(0xFF995454)),
                ) {
                    Text(
                        text = "Exclude",
                        color = Color(0xFFC93333),
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}