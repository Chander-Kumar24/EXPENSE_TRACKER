package com.project.spendtrackrq.presentation.features.banktxn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.spendtrackrq.R;
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.spendtrackrq.presentation.common.UICommonTxnCard
import com.project.spendtrackrq.utils.AllPreviews


@AllPreviews
@Composable
fun ShowBXN()
{
    UIBankTxnImport(rememberNavController())
}

@Composable
fun UIBankTxnImport(navController: NavHostController)
{
    val isWarning = false
    val desc = "Jan 12, 2022"

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (banner, card, backIcon, title, doneIcon) = createRefs()
                Image(
                    painter = painterResource(R.drawable.defaultupbanner),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max=280.dp)
                        .constrainAs(banner)
                        {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.FillBounds
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .constrainAs(backIcon) {
                            top.linkTo(parent.top, margin = 16.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp).rotate(45f) // A common size for icons
                    )
                }

                Text(
                    text = "Review Transactions",
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.constrainAs(title) {
                        // Center Horizontally
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(backIcon.top)
                        bottom.linkTo(backIcon.bottom)

                        //width = Dimension.fillToConstraints

                    }
                )

                IconButton(
                    onClick = { /*Handle import here*/ },
                    modifier = Modifier
                        .constrainAs(doneIcon) {
                            top.linkTo(parent.top, margin = 16.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                        }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.done),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp) // A common size for icons
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min=250.dp, max=280.dp)
                        .padding(horizontal = 16.dp)
                        .shadow(48.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2F7E79))
                        .constrainAs(card) {
                            top.linkTo(banner.top, margin = 110.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp, vertical=16.dp)
                    )
                {
                    Row(horizontalArrangement = Arrangement.SpaceAround)
                    {
                        Text(
                            text = "State Bank of India",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }

                    Text(
                        text = "May 2025",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )

                    Spacer(Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    )
                    {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFD0E5E4),
                                        fontSize = 16.sp
                                    )
                                ) {
                                    append("Total Amount: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                ) {
                                    append("$1,192")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFD0E5E4),
                                        fontSize = 16.sp
                                    )
                                ) {
                                    append("New Transactions: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                ) {
                                    append("52")
                                }
                            }
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp))

                        {
                            Icon(painter=painterResource(R.drawable.incomicon),
                                contentDescription = "Income",
                                modifier=Modifier.size(24.dp),
                                tint = Color.White)

                            Text(
                                text="Income",
                                color=Color(0xFFD0E5E4),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                modifier=Modifier.padding(top=2.dp)
                            )

                            Spacer(Modifier.weight(1f))

                            Icon(painter=painterResource(R.drawable.incomicon),
                                contentDescription = "Income",
                                modifier=Modifier.size(24.dp).rotate(180f),
                                tint = Color.White)

                            Text(
                                text="Expenses",
                                color=Color(0xFFD0E5E4),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                modifier=Modifier.padding(top=2.dp)
                            )
                        }
                        //Spacer(Modifier.height(2.dp))

                        Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween)
                        {
                            BasicText(
                                modifier = Modifier.padding(top=2.dp),
                                text="$1,800.00",
                                style = LocalTextStyle.current.copy(
                                    color=Color.White,
                                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                ),
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = 12.sp,
                                    maxFontSize = 20.sp,
                                )

                            )


                            Spacer(Modifier.weight(1f))

                            BasicText(
                                modifier = Modifier.padding(top=2.dp),
                                text="$2,800.00",
                                style = LocalTextStyle.current.copy(
                                    color=Color.White,
                                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize= 18.sp
                                ),
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = 12.sp,
                                    maxFontSize = 20.sp,
                                )
                            )


                        }
                    }
                }

            }
            }

            Spacer(Modifier.height(28.dp))
           

            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Filter",
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = Color(0xFF222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Spacer(Modifier.weight(1f))

                IconButton(onClick = {})
                {
                    Icon(
                        painter = painterResource(R.drawable.bx_sort_1),
                        contentDescription = "Filter",
                        modifier = Modifier.size(20.dp),
                        tint=Color.Unspecified
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                items(15) {
                    UICommonTxnCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),

                    )
                    {
                        Column(
                            modifier = Modifier.fillMaxHeight().padding(
                                top = 16.dp
                            ).padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        )
                        {

                            Row(modifier=Modifier.fillMaxWidth())
                            {
                                Text(
                                    text = "Starbucks",
                                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF000000),

                                    )
                                Spacer(Modifier.weight(1f))

                                Text(
                                    text="-$ 1,500",
                                    color=Color(0xFFF95B51),
                                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier=Modifier.padding(end=6.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.height(IntrinsicSize.Min),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            )
                            {
                                Text(
                                    text = desc,
                                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF666666)
                                )
                                CircleDivider(modifier = Modifier.align(Alignment.CenterVertically))
                                Text(
                                    text = "@shubhamyaa",
                                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF666666)
                                )
                                if(isWarning)
                                {
                                    Image(
                                        painter = painterResource(R.drawable.warning_txn),
                                        contentDescription = null,
                                        modifier=Modifier.fillMaxSize(1f),

                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }


@Composable
fun CircleDivider(modifier:Modifier= Modifier,
                  size: Dp=5.dp,
                  color: Color=Color.Gray
                  )
{
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )

}