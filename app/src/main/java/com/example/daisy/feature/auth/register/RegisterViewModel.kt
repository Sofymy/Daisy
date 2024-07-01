package com.example.daisy.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.data.utils.UiEvent
import com.example.daisy.domain.usecases.auth.RegisterUseCase
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {


    private var _uiEvent = Channel<UiEvent<AuthResult>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var _registerState = MutableStateFlow(RegisterState())
    var registerState = _registerState

    fun onEvent(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _registerState.update { it.copy(email = newEmail) }
            }
            is RegisterEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _registerState.update { it.copy(password = newPassword) }
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                val newConfirmPassword = event.password.trim()
                _registerState.update { it.copy(confirmPassword = newConfirmPassword) }
            }
            RegisterEvent.PasswordVisibilityChanged -> {
                _registerState.update { it.copy(passwordVisibility = !_registerState.value.passwordVisibility) }
            }
            RegisterEvent.ConfirmPasswordVisibilityChanged -> {
                _registerState.update { it.copy(confirmPasswordVisibility = !_registerState.value.confirmPasswordVisibility) }
            }
            RegisterEvent.Register -> {
                register(
                    registerState.value.email,
                    registerState.value.password
                )
            }
        }
    }

    private fun register(email: String, password: String) = viewModelScope.launch {
        registerUseCase.invoke(email, password).collect {
            _uiEvent.send(it)
        }
    }

}