package com.project.spendtrackrq.presentation.features.budget.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.spendtrackrq.R
import com.project.spendtrackrq.presentation.common.MyCustomProgressBar
import com.project.spendtrackrq.utils.AllPreviews

@AllPreviews
@Composable
fun UIBudgetCatCard(
    cardModifier: Modifier = Modifier,
    categoryIcon: Int = R.drawable.food_active,
    spentAmt: Int = 0,
    progress: Float = 0.5f,
    remAmt: Int = 0,
    totalAmt: Int = 0,
    catName: String = "",
    onClick: () -> Unit = {},
)
{
    Card(
        modifier = cardModifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Color.White)
    )
    {
        Column(
            modifier=Modifier.fillMaxHeight())
        {
            Row(modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                Image(
                    painter = painterResource(categoryIcon),
                    contentDescription = null,
                    modifier=Modifier
                        .width(48.dp)
                        .padding(top = 8.dp)
                )

                Text(
                    text=catName,
                    color= Color(0xFF000000),
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text="$${totalAmt}",
                    color= Color(0xFF000000),
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

            }
            MyCustomProgressBar(maxHeight = 2, padding_top = 8, progress = progress)

            Row(modifier=Modifier
                .fillMaxWidth()
                .padding(top = 8.dp))
            {
                Text( text="Spent: $${spentAmt}",
                    color= Color(0xFF626464),
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.weight(1f))

                Text(
                    text="Remaining: $${remAmt}",
                    color= Color(0xFFCC4545),
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )


            }

        }

    }
}
