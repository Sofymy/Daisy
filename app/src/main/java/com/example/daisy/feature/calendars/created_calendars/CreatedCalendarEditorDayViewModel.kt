package com.example.daisy.feature.calendars.created_calendars

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.Day
import com.example.daisy.domain.model.toDomain
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.DayUi
import com.example.daisy.ui.model.DaysUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreatedCalendarEditorDayUiState(
    val calendarId: String = "",
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val days: DaysUi? = null,
    val day: DayUi? = null
)

sealed class CreatedCalendarEditorDayUserEvent {
    data class GetCreatedCalendarDay(val id: String, val number: Int) : CreatedCalendarEditorDayUserEvent()
    data class MessageChanged(val message: String) : CreatedCalendarEditorDayUserEvent()
    data class SaveModifications(val number: Int, val id: String?) : CreatedCalendarEditorDayUserEvent()
}

@HiltViewModel
class CreatedCalendarEditorDayViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(CreatedCalendarEditorDayUiState())
    val state = _state

    fun onEvent(event: CreatedCalendarEditorDayUserEvent) {
        when (event) {
            is CreatedCalendarEditorDayUserEvent.GetCreatedCalendarDay -> {
                getCreatedCalendarDay(event.id, event.number)
            }

            is CreatedCalendarEditorDayUserEvent.MessageChanged -> {
                updateMessage(event.message)
            }

            is CreatedCalendarEditorDayUserEvent.SaveModifications -> {
                saveModifications(event.number, event.id)
            }
        }
    }

    private fun getCreatedCalendarDay(id: String, number: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val calendar = calendarUseCases.getCreatedCalendarDayUseCase(id, number).getOrThrow()?.toUi()
                val days = calendar?.days
                val day = days?.days?.last { it.number == number }

                _state.update {
                    it.copy(isLoading = false, days = days, day = day, calendarId = id)
                }
            }.onFailure { error ->
                Log.e("ViewModel", "Error retrieving calendar day: ${error.message}")
                _state.update { it.copy(isLoading = false, error = error) }
            }
        }
    }

    private fun updateMessage(message: String) {
        _state.update { currentState ->
            currentState.copy(day = currentState.day?.copy(message = message))
        }
    }


    private fun saveModifications(number: Int, id: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true) }
                _state.value.day?.let { day ->
                    calendarUseCases.saveDayModificationsUseCase(
                        day = day.toDomain(),
                        id = id
                    )
                    _state.update { it.copy(isLoading = false) }
                } ?: run {
                    Log.e("ViewModel", "No day found for number: $number")
                    _state.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error saving modifications: ${e.message}")
            }
        }
    }
}
