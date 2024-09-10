package com.example.daisy.feature.auth.register

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.ui.util.RegisterValidation
import com.example.daisy.ui.util.UiEvent
import com.example.daisy.ui.util.ValidateRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisibility: Boolean = false,
    val confirmPasswordVisibility: Boolean = false,
    val registerValidation: RegisterValidation = RegisterValidation()
)

sealed class RegisterUserEvent {
    data class EmailChanged(val email: String): RegisterUserEvent()
    data class PasswordChanged(val password: String): RegisterUserEvent()
    data class ConfirmPasswordChanged(val password: String): RegisterUserEvent()
    data object PasswordVisibilityChanged: RegisterUserEvent()
    data object ConfirmPasswordVisibilityChanged: RegisterUserEvent()
    data object RegisterUser: RegisterUserEvent()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state

    init {
        observeStateChanges()
    }

    fun onEvent(event: RegisterUserEvent) {
        when (event) {
            is RegisterUserEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }
            is RegisterUserEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(password = newPassword) }
            }
            is RegisterUserEvent.ConfirmPasswordChanged -> {
                val newConfirmPassword = event.password.trim()
                _state.update { it.copy(confirmPassword = newConfirmPassword) }
            }
            RegisterUserEvent.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }
            RegisterUserEvent.ConfirmPasswordVisibilityChanged -> {
                _state.update { it.copy(confirmPasswordVisibility = !state.value.confirmPasswordVisibility) }
            }
            RegisterUserEvent.RegisterUser -> {
                register()
            }
        }
    }

    private fun register() = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCases.registerUseCase(state.value.email, state.value.password)
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun observeStateChanges() {
        viewModelScope.launch {
            state.collect { uiState ->
                    onStateChanged(uiState)
                }
        }
    }

    private fun onStateChanged(newState: RegisterUiState) {
        val validation = ValidateRegister()

        val validationResult = validation.execute(
            email = newState.email,
            password = newState.password,
            passwordConfirm = newState.confirmPassword
        )

        _state.update { it.copy(registerValidation = validationResult) }

    }
}