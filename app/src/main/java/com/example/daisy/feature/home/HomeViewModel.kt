package com.example.daisy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.feature.received_calendars.ReceivedCalendarsUserEvent
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val currentUser: FirebaseUser? = null,
)

sealed class HomeUserEvent {
    data object GetCurrentUser : HomeUserEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state

    fun onEvent(event: HomeUserEvent) {
        when(event){
            HomeUserEvent.GetCurrentUser -> {
                getCurrentUser()
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val result = authUseCases.getCurrentUserUseCase()
                _state.update { it.copy(currentUser = result) }
                _state.update { it.copy(isLoading = false) }
            } catch (_: Exception) {
            }
        }
    }

}