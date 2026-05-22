package com.project.spendtrackrq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.spendtrackrq.presentation.features.onboard.MainViewModel
import com.project.spendtrackrq.presentation.features.onboard.StartDestinationState
import com.project.spendtrackrq.presentation.navigation.components.NavigationScreen
import com.project.spendtrackrq.presentation.theme.SpendTrackrQTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //This single function call is what tells app to draw behind the system bars.
        //enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            SpendTrackrQTheme(darkTheme = darkTheme) {
                val mainViewModel: MainViewModel = hiltViewModel()
                val startState by mainViewModel.startDestination.collectAsState()
                Box(modifier = Modifier.fillMaxSize())
                {
                    when (startState) {
                        is StartDestinationState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        is StartDestinationState.Onboarding -> {
                            MainApp(NavigationScreen.OnBoarding.route)

                        }

                        is StartDestinationState.Home -> {
                            MainApp(NavigationScreen.Home.route)
                        }
                    }
                }

            }
            }
        }
    }


