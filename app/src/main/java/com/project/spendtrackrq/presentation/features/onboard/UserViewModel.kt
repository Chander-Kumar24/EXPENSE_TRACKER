package com.project.spendtrackrq.presentation.features.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spendtrackrq.data.local.dao.UserDAO
import com.project.spendtrackrq.data.local.entities.user.UserEntity
import com.project.spendtrackrq.data.state.RegistrationUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegistrationUiEvent {
    object NavigateToHome : RegistrationUiEvent()
    data class showSnackbar(val message: String) : RegistrationUiEvent()
}

@HiltViewModel
class UserViewModel @Inject constructor(private val userDAO: UserDAO) : ViewModel() {

    //Flows whether we have correct state or note
    private val _uiState = MutableStateFlow(RegistrationUIState())

    //val uiState: MutableStateFlow<RegistrationUIState> = _uiState
    val uiState: StateFlow<RegistrationUIState> =
        _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val getUserState: StateFlow<UserEntity?> = userDAO.observeUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    private fun validateInput(name: String, email: String, phone: String): String? {
        return when {
            name.isBlank() || email.isBlank() || phone.isBlank() -> "All fields are required!"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format!"
            else -> null
        }
    }


    fun onNameChanged(name: String) {
        _uiState.update {
            it.copy(
                name = name
            )
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onPhoneChanged(phone: String) {
        _uiState.update {
            it.copy(
                phone = phone
            )
        }
    }


    fun submitForm() {
        val state = _uiState.value
        val validationError = validateInput(state.name, state.email, state.phone)
        if (validationError != null) {
            viewModelScope.launch {
                _eventFlow.emit(RegistrationUiEvent.showSnackbar(validationError))
            }
            return
        }

        viewModelScope.launch {
            val user = UserEntity(
                name = state.name.trim(),
                email = state.email.trim(),
                phoneNumber = state.phone.trim(),
                completedOnboarding = true
            )

            userDAO.insertUser(user)
            _eventFlow.emit(RegistrationUiEvent.NavigateToHome)
        }


        //_uiState.update { it.copy(errorMessage = null, isSuccess = true) } // we wont update state for this using 'event' is better
    }

    fun updateProfile(user: UserEntity, name: String, email: String, phone: String) {
        val validationError = validateInput(name, email, phone)
        if (validationError != null) {
            viewModelScope.launch {
                _eventFlow.emit(RegistrationUiEvent.showSnackbar(validationError))
            }
            return
        }

        viewModelScope.launch {
            userDAO.insertUser(
                user.copy(
                    name = name.trim(),
                    email = email.trim(),
                    phoneNumber = phone.trim()
                )
            )
            _eventFlow.emit(RegistrationUiEvent.showSnackbar("Profile updated successfully"))
        }
    }
}
