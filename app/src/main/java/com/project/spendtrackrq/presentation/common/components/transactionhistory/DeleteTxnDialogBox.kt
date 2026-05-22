package com.project.spendtrackrq.presentation.common.components.transactionhistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.project.spendtrackrq.R
import com.project.spendtrackrq.presentation.common.UICommonButton
import com.project.spendtrackrq.presentation.theme.redColor


@Composable
fun UIDialogBoxDeleteTxn(
    onDismiss: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
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
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(R.drawable.vector),
                        tint = Color.Unspecified,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = "Delete Transaction?",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp)
                )


                Spacer(Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        "This will permanently delete these transactions.",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "This action cannot be undone.",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "CANCEL",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable { onDismiss() }
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    UICommonButton(
                        "DELETE",
                        textSize = 16.sp,
                        onClick = onDelete,
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                            .align(Alignment.CenterVertically),
                        gradientColors = redColor
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogContentPreview() {
    UIDialogBoxDeleteTxn()
}
