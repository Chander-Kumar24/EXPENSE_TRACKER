package com.project.spendtrackrq.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MyCustomProgressBar(progress: Float = 0.44f,maxHeight:Int =8,padding_top:Int=16, horizontalPadding:Int=0)
{

    Box(modifier=Modifier.fillMaxWidth(1f)
        .padding(top = padding_top.dp)
        .padding(horizontal = horizontalPadding.dp)
        .height(maxHeight.dp)
        .clip(RoundedCornerShape(64.dp))
        .background(Color(0xFFE0E0E0))
    )
    {
        Box(modifier=Modifier.fillMaxWidth(progress)
            .height(maxHeight.dp)
            .background(Color(0xFF198038))
        )

    }
}