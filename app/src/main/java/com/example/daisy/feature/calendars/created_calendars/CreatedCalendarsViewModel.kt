package com.example.daisy.feature.calendars.created_calendars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CreatedCalendarsUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val calendars: List<CalendarUi> = emptyList()
)

sealed class CreatedCalendarsUserEvent {
    data object RefreshCreatedCalendars: CreatedCalendarsUserEvent()
    data object GetCreatedCalendars : CreatedCalendarsUserEvent()
}


@HiltViewModel
class CreatedCalendarsViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private var _state = MutableStateFlow(CreatedCalendarsUiState())
    var state = _state

    fun onEvent(event: CreatedCalendarsUserEvent) {
        when(event) {
            CreatedCalendarsUserEvent.GetCreatedCalendars -> {
                getCreatedCalendars()
            }

            CreatedCalendarsUserEvent.RefreshCreatedCalendars -> {
                refreshCreatedCalendars()
            }
        }
    }

    private fun getCreatedCalendars() {
        viewModelScope.launch {
            try {
                val calendars = calendarUseCases.getCreatedCalendarsUseCase().getOrThrow().map { it?.toUi() ?: CalendarUi() }
                calendars.forEach {
                    it.drawing = calendarUseCases.getCalendarDrawingUseCase(it.id).getOrNull()
                }
                _state.update { it.copy(
                    isLoading = false,
                    calendars = calendars
                ) }
            } catch (e: Exception) {
                _state.update {  it.copy(
                    isLoading = false,
                    error = e
                ) }
            }
        }
    }

    private fun refreshCreatedCalendars() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(
                    isRefreshing = true,
                ) }
                val calendars = calendarUseCases.getCreatedCalendarsUseCase().getOrThrow().map { it?.toUi() ?: CalendarUi() }
                calendars.forEach {
                    it.drawing = calendarUseCases.getCalendarDrawingUseCase(it.id).getOrNull()
                }
                _state.update { it.copy(
                    isRefreshing = false,
                    calendars = calendars
                ) }
            } catch (e: Exception) {
                _state.update {  it.copy(
                    isLoading = false,
                    error = e
                ) }
            }
        }
    }

}