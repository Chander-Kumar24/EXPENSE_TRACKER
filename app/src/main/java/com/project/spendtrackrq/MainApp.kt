package com.project.spendtrackrq

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.project.spendtrackrq.presentation.navigation.BottomNavigationGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp(startDestination: String) {
    val navController = rememberNavController()

    Scaffold { _ ->
        Box {
            BottomNavigationGraph(startDestination, navController)
        }
    }
}