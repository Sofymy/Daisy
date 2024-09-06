package com.example.daisy.feature.auth.onboarding

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

data class OnboardingUiState(
    val isLoading: Boolean = true,
    val isSignedIn: Boolean? = null
)

sealed class OnboardingUserEvent {
    data object IsSignedId: OnboardingUserEvent()
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state = _state

    fun onEvent(event: OnboardingUserEvent) {
        when(event) {
            is OnboardingUserEvent.IsSignedId -> {
                isSignedIn()
            }
        }
    }

    private fun isSignedIn() {
        viewModelScope.launch {
            try {
                val result = authUseCases.isSignedInUseCase()
                _state.update { it.copy(isSignedIn = result, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

}