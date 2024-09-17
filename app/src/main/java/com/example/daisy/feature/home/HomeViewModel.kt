package com.example.daisy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.Calendar
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.feature.created_calendars.CreatedCalendarsUserEvent
import com.example.daisy.feature.received_calendars.ReceivedCalendarsUserEvent
import com.example.daisy.ui.model.CalendarUi
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val currentUser: FirebaseUser? = null,
    val receivedCalendars: List<CalendarUi> = emptyList(),
    val createdCalendars: List<CalendarUi> = emptyList()
)

sealed class HomeUserEvent {
    data object GetCurrentUser : HomeUserEvent()
    data object GetReceivedCalendars : HomeUserEvent()
    data object GetCreatedCalendars : HomeUserEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state

    fun onEvent(event: HomeUserEvent) {
        when(event){
            HomeUserEvent.GetCurrentUser -> {
                getCurrentUser()
            }

            HomeUserEvent.GetCreatedCalendars -> {
                getCreatedCalendars()
            }
            HomeUserEvent.GetReceivedCalendars -> {
                getReceivedCalendars()
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

    private fun getCreatedCalendars() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val calendars = calendarUseCases.getCreatedCalendarsUseCase().getOrThrow().map { it?.toUi() ?: CalendarUi() }
                    _state.update { it.copy(
                        isLoading = false,
                        createdCalendars = calendars
                    ) }
                }
            } catch (e: Exception) {
                _state.update {  it.copy(
                    isLoading = false,
                ) }
            }
        }
    }

    private fun getReceivedCalendars() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val calendars = calendarUseCases.getReceivedCalendarsUseCase().getOrThrow().map { it?.toUi() ?: CalendarUi() }

                    _state.update { it.copy(
                        isLoading = false,
                        receivedCalendars = calendars
                    ) }
                }
            } catch (e: Exception) {
                _state.update {  it.copy(
                    isLoading = false,
                ) }
            }
        }
    }
}