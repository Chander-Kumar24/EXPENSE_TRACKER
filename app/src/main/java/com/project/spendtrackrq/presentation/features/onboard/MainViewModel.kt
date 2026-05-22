package com.project.spendtrackrq.presentation.features.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spendtrackrq.data.local.dao.UserDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class StartDestinationState {
    object Loading : StartDestinationState()
    object Onboarding : StartDestinationState()
    object Home : StartDestinationState()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val userDao: UserDAO) : ViewModel() {
    //hold the result, start loading state
    private val _startDestination =
        MutableStateFlow<StartDestinationState>(StartDestinationState.Loading)
    val startDestination = _startDestination.asStateFlow()

    //run this when this viewmodel created
    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            userDao.observeUser().collect { user ->
                if (user != null && user.completedOnboarding) {
                    _startDestination.value = StartDestinationState.Home
                } else {
                    _startDestination.value = StartDestinationState.Onboarding
                }
            }
        }
    }
}
