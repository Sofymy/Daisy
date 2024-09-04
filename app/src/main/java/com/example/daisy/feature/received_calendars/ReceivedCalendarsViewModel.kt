package com.example.daisy.feature.received_calendars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ReceivedCalendarsUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val calendars: List<CalendarUi> = emptyList()
)

sealed class ReceivedCalendarsUserEvent {
    data object GetReceivedCalendars : ReceivedCalendarsUserEvent()
}


@HiltViewModel
class ReceivedCalendarsViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private var _state = MutableStateFlow(ReceivedCalendarsUiState())
    var state = _state

    fun onEvent(event: ReceivedCalendarsUserEvent) {
        when(event) {
            ReceivedCalendarsUserEvent.GetReceivedCalendars -> {
                getReceivedCalendars()
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

}