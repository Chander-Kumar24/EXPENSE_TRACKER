package com.project.spendtrackrq.presentation.features.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.navigation.compose.rememberNavController
import com.project.spendtrackrq.R
import com.project.spendtrackrq.presentation.common.UICommonBottomBar
import com.project.spendtrackrq.presentation.common.UICommonButton
import com.project.spendtrackrq.presentation.common.UITabRow
import com.project.spendtrackrq.presentation.theme.greenColor
import com.project.spendtrackrq.utils.AllPreviews

//TODO: FAB action button will have speed dial of "Set Monthly" "Set Weekly" "Set Custom" "Add Category"
@AllPreviews
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoBudgetScreen(navController: NavHostController=rememberNavController()) {
    Scaffold(
        bottomBar = { UICommonBottomBar(navController) },
        topBar = { UITabRow(navController, "Budget") },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "You haven't set any budget yet.\nLet's get started!",
                lineHeight = 36.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color(0xFF1F615C),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(48.dp))
            UICommonButton(
                "Set Budget",
                18.sp,
                { navController.navigate("setBudgetScreen") },
                gradientColors = greenColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.7f)
            )
        }
    }
}
