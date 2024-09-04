package com.example.daisy.feature.new_calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toDomain
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.RecipientOptionUi
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

data class NewCalendarUiState(
    val calendar: CalendarUi,
    val calendarOption: RecipientOptionUi.RecipientByCode
)

sealed class NewCalendarUserEvent {
    data class StartChanged(val dateStart: LocalDate): NewCalendarUserEvent()
    data class EndChanged(val dateEnd: LocalDate): NewCalendarUserEvent()
    data class RecipientNameChanged(val recipientName: String): NewCalendarUserEvent()
    data class RecipientEmailChanged(val recipientEmail: String): NewCalendarUserEvent()
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
                _state.update { it.copy(dateRange = it.dateRange.copy(dateStart = event.dateStart)) }
            }

            is NewCalendarUserEvent.EndChanged -> {
                _state.update { it.copy(dateRange = it.dateRange.copy(dateEnd = event.dateEnd)) }
            }

            is NewCalendarUserEvent.RecipientNameChanged -> {
                _state.update { it.copy(recipient = it.recipient.copy(name = event.recipientName)) }
            }

            is NewCalendarUserEvent.RecipientEmailChanged -> {
                _state.update { it.copy(recipient = it.recipient.copy(email = event.recipientEmail)) }
            }

            is NewCalendarUserEvent.CreateCalendar -> {
                createCalendar()
            }

        }
    }

    private fun createCalendar() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                calendarUseCases.setCalendarUseCase(state.value.toDomain())
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

}