package com.project.spendtrackrq.presentation.features.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.spendtrackrq.R
import com.project.spendtrackrq.presentation.common.UICommonBanner
import com.project.spendtrackrq.presentation.common.UICommonButton
import com.project.spendtrackrq.presentation.theme.greenColor
import com.project.spendtrackrq.utils.AllPreviews


@AllPreviews
@Composable
fun ShowLoginScreen()
{
    UILoginScreen()
}

@Composable
fun UILoginScreen() {

        var phoneNumber by remember { mutableStateOf("") }

            UICommonBanner(
                mainTitle = "Get Started",
                "Enter your phone number to continue"
            )
            {
                Spacer(modifier = Modifier.height(48.dp))

                /*OutlinedTextField(
                    value = phoneNumber, // Current value of the text field
                    onValueChange = { newValue ->
                        phoneNumber = newValue
                    }, // Update the state when text changes
                    label = { Text("Enter your Phone Number") }, // More descriptive label
                    modifier = Modifier
                        .fillMaxWidth() // Make the text field fill the width
                        .padding(horizontal = 16.dp) // Add horizontal padding
                )*/

                UICommonButton("CLICK HERE TO CONTINUE", 18.sp, {},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 36.dp), gradientColors = greenColor
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp)
                        .padding(horizontal = 56.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(0.3f),
                        thickness = 1.dp,
                        color = Color(0xFFCCCCCC)
                    )

                    Text(
                        text = "OR SIGN UP WITH",
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(0.3f),
                        thickness = 1.dp,
                        color = Color(0xFFCCCCCC)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                )
                {
                    UILoginCards(R.drawable.google)
                    UILoginCards(R.drawable.f_logo_rgb_blue_144_1)
                    UILoginCards(R.drawable.apple_black_1)
                    UILoginCards(R.drawable.twitter_color_1)
                }

            }
        }

@Composable
fun UILoginCards(imgResId: Int) {
    //Spacer(modifier = Modifier.height(24.dp))
    Box(
        modifier = Modifier
            .size(64.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = imgResId),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}