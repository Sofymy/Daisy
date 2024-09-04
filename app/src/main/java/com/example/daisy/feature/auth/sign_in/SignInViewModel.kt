package com.example.daisy.feature.auth.sign_in

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

data class SignInUiState(
    val isLoading: Boolean = true,
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
    val isSignedIn: Boolean? = null
)

sealed class SignInUserEvent {
    data class EmailChanged(val email: String): SignInUserEvent()
    data class PasswordChanged(val password: String): SignInUserEvent()
    data class SignInWithGoogle(val token: String?, val email: String?) : SignInUserEvent()
    data object PasswordVisibilityChanged: SignInUserEvent()
    data object SignIn: SignInUserEvent()
    data object IsSignedId: SignInUserEvent()
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SignInUiState())
    val state = _state

    private var _uiEvent = Channel<UiEvent>()
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
                signInWithGoogle(event.token, event.email)
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
                _state.update { it.copy(isSignedIn = result, isLoading = false) }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCases.signInUseCase(state.value.email, state.value.password)
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun signInWithGoogle(token: String?, email: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (token != null && email != null) {
                    val result = authUseCases.signInWithGoogleUseCase(token, email)
                    if(result.isSuccess)
                        _uiEvent.send(UiEvent.Success)
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

}