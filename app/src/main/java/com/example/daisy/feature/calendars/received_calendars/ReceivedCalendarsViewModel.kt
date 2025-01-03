package com.example.daisy.feature.calendars.received_calendars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarsUserEvent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ReceivedCalendarsUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val calendars: List<CalendarUi> = emptyList()
)

sealed class ReceivedCalendarsUserEvent {
    data object RefreshReceivedCalendars: ReceivedCalendarsUserEvent()
    data object GetReceivedCalendars : ReceivedCalendarsUserEvent()
    data class AddReceivedCalendarByCode(val code: String) : ReceivedCalendarsUserEvent()
}


@HiltViewModel
class ReceivedCalendarsViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private var _state = MutableStateFlow(ReceivedCalendarsUiState())
    var state = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ReceivedCalendarsUserEvent) {
        when(event) {
            is ReceivedCalendarsUserEvent.GetReceivedCalendars -> {
                getReceivedCalendars()
            }

            is ReceivedCalendarsUserEvent.AddReceivedCalendarByCode -> {
                addReceivedCalendarByCode(event.code)
            }

            is ReceivedCalendarsUserEvent.RefreshReceivedCalendars -> {
                refreshReceivedCalendars()
            }
        }
    }

    private fun addReceivedCalendarByCode(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(
                    isLoading = true,
                ) }
                val result = calendarUseCases.addReceivedCalendarByCodeUseCase(code)
                if (result.isSuccess) getReceivedCalendars()
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun getReceivedCalendars() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val calendars = calendarUseCases.getReceivedCalendarsUseCase().getOrThrow().map { it?.toUi() ?: CalendarUi() }

                    calendars.forEach {
                        it.drawing = calendarUseCases.getCalendarDrawingUseCase(it.id).getOrNull()
                    }

                    _state.update { it.copy(
                        isLoading = false,
                        calendars = calendars
                    ) }
                }
            } catch (e: Exception) {
                _state.update {  it.copy(
                    isLoading = false,
                    error = e
                ) }
            }
        }
    }

    private fun refreshReceivedCalendars() {
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