package com.project.spendtrackrq.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.spendtrackrq.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowCommonTxnCard()
{
    val desc = "Jan 12, 2022"
    UICommonTxnCard(modifier =
        Modifier
            .padding(top = 42.dp)
            .width(380.dp)
            .height(150.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            Color.Transparent, //Color(0xFFF0F6F5)
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center)
                {
                    Image(
                        painter = painterResource(id = R.drawable.food_active),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Starbucks",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF000000)
                    )

                    Text(
                        text = desc,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666)
                    )
                }
                Spacer(Modifier.weight(1f))

                Text(
                    text="$700",
                    color=Color(0xFF4B4B4B),
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier=Modifier.padding(end=6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UICommonTxnCard(
    modifier: Modifier = Modifier,
    cardBgColor: Color = Color(0xFFFBFBFB),
    shape: Shape = CardDefaults.shape,
    border: BorderStroke? = null,
    elevation: CardElevation = CardDefaults.cardElevation(),
    content: @Composable () -> Unit,

    )
{

    Card(
        modifier=modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor
        ),
        elevation = elevation,
        shape = shape,
        border = border
    )
    {

        content()
    }

}



