package com.project.spendtrackrq.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.project.spendtrackrq.R
import com.project.spendtrackrq.utils.AllPreviews


@AllPreviews
@Composable
fun ShowCommonButton()
{
    // UICommonButton("Send OTP", 18.sp ,{})
}

@Composable
fun UICommonButton(btnText: String,
                   textSize: TextUnit,
                   onClick: () -> Unit,
                   fontWeight: FontWeight = FontWeight.SemiBold,
                   gradientColors: Array<Pair<Float, Color>>,
                   @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
)
{


    ElevatedButton(
        onClick = onClick,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 16.dp),
        modifier = modifier
            .shadow(8.dp, shape = ButtonDefaults.elevatedShape)
            .background(
                Brush.verticalGradient(colorStops = gradientColors),
                shape = ButtonDefaults.elevatedShape
            ),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        )
        ) {
        Text(btnText, fontSize = textSize, fontWeight = fontWeight, fontFamily = FontFamily(Font(R.font.inter_medium)))
    }
}