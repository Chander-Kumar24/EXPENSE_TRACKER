package com.project.spendtrackrq.presentation.features.budget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun BudgetScreen(
    navController: NavHostController,
    viewModel: BudgetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.budgetUIState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is BudgetUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is BudgetUIState.HasBudget -> {
                UIMainBudgetScreen(
                    navController = navController,
                    budgetViewModel = viewModel,
                    onEditClick = {
                        navController.navigate("setBudgetScreen?isEdit=true") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    isEditMode = true
                )
            }

            is BudgetUIState.NoBudget -> {
                NoBudgetScreen(navController = navController)
            }

            is BudgetUIState.Error -> {
                NoBudgetScreen(navController = navController)
            }
        }
    }
}
