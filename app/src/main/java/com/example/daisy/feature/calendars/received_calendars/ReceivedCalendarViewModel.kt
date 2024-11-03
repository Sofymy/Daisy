package com.example.daisy.feature.calendars.received_calendars

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.DateRangeUi
import com.example.daisy.ui.model.DaysUi
import com.example.daisy.ui.model.IconOptionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

data class ReceivedCalendarUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val calendar: CalendarUi? = CalendarUi()
)

sealed class ReceivedCalendarUserEvent {
    data class GetReceivedCalendar(val id: String) : ReceivedCalendarUserEvent()
}

@HiltViewModel
class ReceivedCalendarViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ReceivedCalendarUiState())
    val state = _state

    fun onEvent(event: ReceivedCalendarUserEvent) {
        when (event) {
            is ReceivedCalendarUserEvent.GetReceivedCalendar -> getReceivedCalendar(event.id)
        }
    }

    private fun getReceivedCalendar(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                calendarUseCases.getCreatedCalendarUseCase(id).getOrThrow()?.toUi()?.also { calendar ->
                    calendar.drawing = calendarUseCases.getCalendarDrawingUseCase(calendar.id).getOrNull()
                    _state.update { it.copy(isLoading = false, calendar = calendar) }
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error) }
            }
        }
    }
}
