package com.project.spendtrackrq.presentation.navigation

//import com.project.spendtrackrq.presentation.features.budget.UISetBudgetScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.project.spendtrackrq.presentation.common.UICommonHome
import com.project.spendtrackrq.presentation.common.UITransactionHistory
import com.project.spendtrackrq.presentation.features.banktxn.UIBankTxnImport
import com.project.spendtrackrq.presentation.features.budget.BudgetScreen
import com.project.spendtrackrq.presentation.features.budget.BudgetViewModel
import com.project.spendtrackrq.presentation.features.budget.UISetBudgetScreen
import com.project.spendtrackrq.presentation.features.expense.ExpenseViewModel
import com.project.spendtrackrq.presentation.features.expense.UIExpenseAdder
import com.project.spendtrackrq.presentation.features.onboard.UIBasicInfoForm
import com.project.spendtrackrq.presentation.features.onboard.UserViewModel
import com.project.spendtrackrq.presentation.features.profile.ProfileScreen
import com.project.spendtrackrq.presentation.features.statsscreen.UIStatsActivity
import com.project.spendtrackrq.presentation.navigation.components.NavigationScreen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun BottomNavigationGraph(startDestination: String, navController: NavHostController)
{
    NavHost(
        navController = navController,
        startDestination = "auth_flow",
        modifier=Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    )
    {
        navigation(
            route = "auth_flow",
            startDestination = startDestination
        ) {
            composable(NavigationScreen.OnBoarding.route) {

                val parentBackStackEntry = remember { navController.getBackStackEntry("auth_flow") }

                val userViewModel: UserViewModel = hiltViewModel(parentBackStackEntry)

                UIBasicInfoForm(userViewModel) {
                    navController.navigate(NavigationScreen.Home.route) {
                        popUpTo(
                            NavigationScreen.OnBoarding.route
                        ) { inclusive = true }
                    }
                }
            }
            composable(NavigationScreen.Home.route) {
                val parentBackStackEntry = remember { navController.getBackStackEntry("auth_flow") }
                val userViewModel: UserViewModel = hiltViewModel(parentBackStackEntry)
                val expenseViewModel: ExpenseViewModel = hiltViewModel()
                UICommonHome(userViewModel, expenseViewModel,navController)

            }
        }
        composable(NavigationScreen.Statistics.route) {
            UIStatsActivity(navController)
        }
        composable(NavigationScreen.Budget.route) {
            BudgetScreen(navController = navController)
        }
        composable(NavigationScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        //Button navs
        navigation(
            route = "transaction_flow", //this viewmodel is persist across every screen that's under 'transaction_flow's
            startDestination = "expenseAdder"
        ) {
            composable(
                "transactionHistory?category={category}", arguments = listOf(
                    navArgument("category") {
                        type = NavType.StringType
                        nullable = true
                    })
            ) { backStackEntry ->
                // Get the back stack entry for the parent navigation graph
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("transaction_flow")
                }
                // Scope the ViewModel to the parent graph
                val expenseViewModel: ExpenseViewModel = hiltViewModel(parentEntry)
                val category = parentEntry.arguments?.getString("category")
                //call me only when category changes just ONCE
                LaunchedEffect(category) {
                    if (category != null && category != "all") {
                        expenseViewModel.loadTransactionByCategory(category)
                    }
                }
                UITransactionHistory(expenseViewModel, navController, category)
            }
            composable(
                "expenseAdder/{transactionId}",
                arguments = listOf(
                    navArgument("transactionId")
                    {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )) { backstackEntry ->
                val getArgument = backstackEntry.arguments?.getInt("transactionId") ?: -1

                // Get the back stack entry for the parent navigation graph
                val parentEntry = remember(backstackEntry) {
                    navController.getBackStackEntry("transaction_flow")
                }
                // Scope the ViewModel to the parent graph
                val expenseViewModel: ExpenseViewModel = hiltViewModel(parentEntry)
                UIExpenseAdder(expenseViewModel, navController, getArgument)
            }
        }


        composable(
            "setBudgetScreen?isEdit={isEdit}",
            arguments = listOf(
                navArgument("isEdit") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val isEdit = backStackEntry.arguments?.getBoolean("isEdit") ?: false
            val budgetViewModel: BudgetViewModel = hiltViewModel()
            UISetBudgetScreen(navController, budgetViewModel, isEdit)
        }


        composable("bankTxnScreen") {
            UIBankTxnImport(navController)
        }


    }


}
