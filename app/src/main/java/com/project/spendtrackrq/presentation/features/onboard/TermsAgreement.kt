package com.project.spendtrackrq.presentation.features.onboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.presentation.common.UICommonButton
import com.project.spendtrackrq.presentation.common.UITabRow
import com.project.spendtrackrq.presentation.theme.greenColor

@Composable
fun DevPlayGround(navController: NavHostController? = null) {
    Scaffold(
        topBar = { UITabRow(navController, text = "Terms and Agreement") },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DECLINE",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { }
                )

                Spacer(modifier = Modifier.width(24.dp))

                UICommonButton(
                    "ACCEPT",
                    textSize = 16.sp,
                    { },
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    gradientColors = greenColor
                )
            }
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        )
        {
            Text(
                text = "Please read and agree to the following Terms " +
                        "and Agreement before using the app:",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )

            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "1. I recognize this app open-source and provided 'as-is' with no" +
                            "warranties, explicit or implicit. I am not responsible for any" +
                            "loss or damage caused by the use of this app.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )
                Text(
                    text = "2. I understand there is no warranty for the accuracy, completeness, or reliability of my information stored in app." +
                            "Manual data backup is my responsibility, and I agree to not hold the app liable for any loss or damage.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )

                Text(
                    text = "4. I am aware and accept that the app may display misleading information or contain inaccuracies." +
                            "I assume full responsibility for verifying the accuracy and integrity of the information displayed" +
                            "and calculation before making any decisions based on app data.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )
                Text(

                    text = "5. In no event shall the developers or contributors be liable for any damages," +
                            " including but not limited to loss of profits, " +
                            "data, or business opportunities, even if advised of the possibility of such damages.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "By installing, accessing, or using this app," +
                        " you agree to these terms. If you do not agree, you must not use the app.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )


        }
    }

}