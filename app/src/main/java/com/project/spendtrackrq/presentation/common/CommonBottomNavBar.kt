package com.project.spendtrackrq.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.project.spendtrackrq.presentation.navigation.components.NavigationScreen
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowBottomAppBar() {
    UIBottomAppBar()
}

@Composable
fun UIBottomAppBar() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ },
                shape=CircleShape,
                modifier = Modifier
                    .size(80.dp)
                    .offset(y = 35.dp),
                containerColor = Color(0xFF438883),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add",
                    modifier = Modifier.size(38.dp), tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            UICommonBottomBar(rememberNavController())
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Main Content")
        }
    }
}

@Composable
fun UICommonBottomBar(navController: NavHostController)
{

    val screens = listOf(NavigationScreen.Home, NavigationScreen.Budget,
        NavigationScreen.Statistics, NavigationScreen.Profile)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    BottomAppBar(containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {


        Row(modifier=Modifier.fillMaxSize().padding(top=8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically)
        {
            screens.forEach { screen ->
                val isSelected = currentRoute == screen.route
                IconButton(onClick = {
                    if (currentRoute != screen.route)
                    {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            {saveState = screen.saveState}
                            launchSingleTop = true
                            restoreState = screen.saveState
                        }
                    }

                }) {
                    Image(
                        painter = painterResource(id = if (isSelected) screen.iconSelected else screen.iconDeselected),
                        contentDescription = null,
                        modifier = Modifier.aspectRatio(1.5f)
                    )
                }

            }


        }

    }
}
