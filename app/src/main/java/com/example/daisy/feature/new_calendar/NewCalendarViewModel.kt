package com.example.daisy.feature.new_calendar

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toDomain
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.DateRangeUi
import com.example.daisy.ui.model.DaysUi
import com.example.daisy.ui.model.IconOptionUi
import com.example.daisy.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

data class NewCalendarUiState(
    val calendar: CalendarUi,
)

sealed class NewCalendarUserEvent {
    data class StartChanged(val dateStart: LocalDate) : NewCalendarUserEvent()
    data class EndChanged(val dateEnd: LocalDate) : NewCalendarUserEvent()
    data object RecipientOptionSelected : NewCalendarUserEvent()
    data object CodeOptionSelected : NewCalendarUserEvent()
    data class TitleChanged(val title: String) : NewCalendarUserEvent()
    data class DrawingChanged(val bitmap: Bitmap) : NewCalendarUserEvent()
    data class IconChanged(val icon: IconOptionUi) : NewCalendarUserEvent()
    data class RecipientEmailChanged(val recipientEmail: String) : NewCalendarUserEvent()
    data object CreateCalendar : NewCalendarUserEvent()
}

@HiltViewModel
class NewCalendarViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var _state = MutableStateFlow(CalendarUi())
    var state = _state

    fun onEvent(event: NewCalendarUserEvent) {
        when (event) {
            is NewCalendarUserEvent.StartChanged -> {
                _state.update { currentState ->
                    val updatedDateRange = DateRangeUi(event.dateStart, currentState.days.dateRange.dateEnd)
                    val updatedDays = DaysUi(dateRange = updatedDateRange)
                    currentState.copy(days = updatedDays)
                }
            }

            is NewCalendarUserEvent.EndChanged -> {
                _state.update { currentState ->
                    val updatedDateRange = DateRangeUi(currentState.days.dateRange.dateStart, event.dateEnd)
                    val updatedDays = DaysUi(dateRange = updatedDateRange)
                    currentState.copy(days = updatedDays)
                }
            }

            is NewCalendarUserEvent.RecipientEmailChanged -> {
                _state.update { currentState ->
                    currentState.copy(recipients = listOf(event.recipientEmail))
                }
            }

            is NewCalendarUserEvent.TitleChanged -> {
                _state.update { currentState ->
                    currentState.copy(title = event.title)
                }
            }

            is NewCalendarUserEvent.CreateCalendar -> {
                createCalendar()
            }

            is NewCalendarUserEvent.CodeOptionSelected -> {
                _state.update { currentState ->
                    currentState.copy(recipients = listOf(""))
                }
                _state.update { currentState ->
                    currentState.copy(code = Random.nextInt(100000, 999999).toString())
                }
            }

            is NewCalendarUserEvent.RecipientOptionSelected -> {
                _state.update { currentState ->
                    currentState.copy(code = null, recipients = listOf(""))
                }
            }

            is NewCalendarUserEvent.IconChanged -> {
                _state.update { currentState ->
                    currentState.copy(icon = event.icon)
                }
            }

            is NewCalendarUserEvent.DrawingChanged -> {
                _state.update { currentState ->
                    currentState.copy(drawing = event.bitmap)
                }
            }

            else -> {}
        }
    }

    private fun createCalendar() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                calendarUseCases.setCalendarUseCase(state.value.toDomain(), state.value.drawing)
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }
}
