package com.project.spendtrackrq.presentation.common

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.data.models.topbar.TopAppBarDTO
import com.project.spendtrackrq.utils.AllPreviews

@AllPreviews
@Composable
fun UITabRow(navController: NavHostController? = null,
             text: String = "",
             actions: List<TopAppBarDTO> = emptyList(),
             isVisible: Boolean = false,
             @SuppressLint("ModifierParameter") modifier:Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    val popped = navController?.popBackStack()
                    if (popped == false) navController.navigate("Home")
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = Color(0xFF666666),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier.align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {

                androidx.compose.animation.AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 150
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        actions.forEach { action ->

                            IconButton(onClick = action.onClick) {
                                Icon(
                                    painter = painterResource(action.icon),
                                    contentDescription = action.contentDescription,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(action.size)
                                )
                            }
                        }

                    }


                }


            }
        }
    }
}
