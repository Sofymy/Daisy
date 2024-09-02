package com.example.daisy.feature.auth.sign_in

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SignInUiState())
    val state = _state

    private var _uiEvent = Channel<UiEvent<Any>>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: SignInUserEvent) {
        when(event) {
            is SignInUserEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }
            is SignInUserEvent.IsSignedId -> {
                isSignedIn()
            }
            is SignInUserEvent.SignIn -> {
                signIn()
            }
            is SignInUserEvent.SignInWithGoogle -> {
                signInWithGoogle(event.token)
            }
            is SignInUserEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(email = newPassword) }
            }
            SignInUserEvent.PasswordVisibilityChanged -> TODO()
        }
    }

    private fun isSignedIn() {
        viewModelScope.launch {
            try {
                val result = authUseCases.isSignedInUseCase()
                _state.update { it.copy(isSignedIn = result) }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCases.signInUseCase(state.value.email, state.value.password)
                _uiEvent.send(UiEvent.Success(null))
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun signInWithGoogle(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (token != null) {
                    val result = authUseCases.signInWithGoogleUseCase(token)
                    if(result.isSuccess)
                        _uiEvent.send(UiEvent.Success(token))
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

}