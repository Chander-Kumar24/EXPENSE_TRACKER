package com.project.spendtrackrq.presentation.navigation.components

import com.project.spendtrackrq.R

sealed class NavigationScreen(val route: String,
                              val title: String,
                              val iconSelected:Int,
                              val iconDeselected:Int,
    val saveState: Boolean = false
) {
    object OnBoarding : NavigationScreen("onboarding", "Onboarding", 0, 0)
    object Home: NavigationScreen("home","Home", R.drawable.home_f_1, R.drawable.home_1, false)
    object Statistics: NavigationScreen("statistics","Statistics", R.drawable.bar_chart_fill_1, R.drawable.bar_chart_1,false)
    object Budget: NavigationScreen("budget", "Budget", R.drawable.accounting__1_, R.drawable.accounting, true)
    object Profile: NavigationScreen("profile", "Profile", R.drawable.user_fill_1, R.drawable.user__1__1,false)
}