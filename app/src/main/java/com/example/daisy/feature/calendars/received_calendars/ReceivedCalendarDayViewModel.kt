package com.example.daisy.feature.calendars.received_calendars

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.DayUi
import com.example.daisy.ui.model.DaysUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReceivedCalendarDayUiState(
    val calendarId: String = "",
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val day: DayUi? = null
)

sealed class ReceivedCalendarDayUserEvent {
    data class GetReceivedCalendarDay(val id: String, val number: Int) : ReceivedCalendarDayUserEvent()
}

@HiltViewModel
class ReceivedCalendarDayViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ReceivedCalendarDayUiState())
    val state = _state

    fun onEvent(event: ReceivedCalendarDayUserEvent) {
        when (event) {
            is ReceivedCalendarDayUserEvent.GetReceivedCalendarDay -> {
                getReceivedCalendarDay(event.id, event.number)
            }
        }
    }

    private fun getReceivedCalendarDay(id: String, number: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val calendar = calendarUseCases.getReceivedCalendarDayUseCase(id, number).getOrThrow()?.toUi()
                val days = calendar?.days
                val day = days?.days?.last { it.number == number }

                _state.update {
                    it.copy(isLoading = false, day = day, calendarId = id)
                }
            }.onFailure { error ->
                Log.e("ReceivedCalendarDaysViewModel", "Error retrieving calendar day: ${error.message}")
                _state.update { it.copy(isLoading = false, error = error) }
            }
        }
    }
}
