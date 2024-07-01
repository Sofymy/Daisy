package com.example.daisy.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.data.utils.UiEvent
import com.example.daisy.domain.usecases.auth.IsLoggedInUseCase
import com.example.daisy.domain.usecases.auth.LoginUseCase
import com.example.daisy.domain.usecases.auth.ResetPasswordUseCase
import com.example.daisy.feature.auth.register.RegisterEvent
import com.example.daisy.feature.auth.register.RegisterState
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState

    private var _uiEvent = Channel<UiEvent<AuthResult>>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _loginState.update { it.copy(email = newEmail) }
            }
            LoginEvent.Has -> {}
            LoginEvent.Login -> {
                login(loginState.value.email, loginState.value.password)
            }
            is LoginEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _loginState.update { it.copy(email = newPassword) }
            }
            LoginEvent.PasswordVisibilityChanged -> TODO()
        }
    }

    private fun login(email: String, password: String) = viewModelScope.launch {
        loginUseCase.invoke(email, password).collect { response ->
            _uiEvent.send(response)
        }
    }

    private fun resetPassword(email: String) = viewModelScope.launch {
        resetPasswordUseCase.invoke(email).collect { response ->
        }
    }

}