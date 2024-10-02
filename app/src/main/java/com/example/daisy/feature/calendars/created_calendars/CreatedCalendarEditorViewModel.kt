package com.example.daisy.feature.calendars.created_calendars

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toDomain
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.DateRangeUi
import com.example.daisy.ui.model.DaysUi
import com.example.daisy.ui.model.IconOptionUi
import com.example.daisy.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

data class CreatedCalendarEditorUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val calendar: CalendarUi? = CalendarUi()
)

sealed class CreatedCalendarEditorUserEvent {
    data class StartChanged(val dateStart: LocalDate) : CreatedCalendarEditorUserEvent()
    data class EndChanged(val dateEnd: LocalDate) : CreatedCalendarEditorUserEvent()
    data object RecipientOptionSelected : CreatedCalendarEditorUserEvent()
    data object CodeOptionSelected : CreatedCalendarEditorUserEvent()
    data class TitleChanged(val title: String) : CreatedCalendarEditorUserEvent()
    data class DrawingChanged(val bitmap: Bitmap) : CreatedCalendarEditorUserEvent()
    data class IconChanged(val icon: IconOptionUi) : CreatedCalendarEditorUserEvent()
    data class RecipientEmailChanged(val recipientEmail: String) : CreatedCalendarEditorUserEvent()
    data class GetCreatedCalendar(val id: String) : CreatedCalendarEditorUserEvent()
    data object SaveModifications : CreatedCalendarEditorUserEvent()
}

@HiltViewModel
class CreatedCalendarViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(CreatedCalendarEditorUiState())
    val state = _state

    fun onEvent(event: CreatedCalendarEditorUserEvent) {
        when (event) {
            is CreatedCalendarEditorUserEvent.GetCreatedCalendar -> getCreatedCalendar(event.id)
            is CreatedCalendarEditorUserEvent.StartChanged -> updateStartDate(event.dateStart)
            is CreatedCalendarEditorUserEvent.EndChanged -> updateEndDate(event.dateEnd)
            is CreatedCalendarEditorUserEvent.RecipientEmailChanged -> updateRecipientEmail(event.recipientEmail)
            is CreatedCalendarEditorUserEvent.TitleChanged -> updateTitle(event.title)
            is CreatedCalendarEditorUserEvent.CodeOptionSelected -> selectCodeOption()
            is CreatedCalendarEditorUserEvent.RecipientOptionSelected -> selectRecipientOption()
            is CreatedCalendarEditorUserEvent.IconChanged -> updateIcon(event.icon)
            is CreatedCalendarEditorUserEvent.DrawingChanged -> updateDrawing(event.bitmap)
            is CreatedCalendarEditorUserEvent.SaveModifications -> saveModifications()
        }
    }

    private fun saveModifications() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(
                    isLoading = true,
                ) }
                state.value.calendar?.toDomain()
                    ?.let { calendarUseCases.saveModificationsUseCase(it) }
                _state.update { it.copy(
                    isLoading = false,
                ) }
            } catch (e: Exception) {
                //_uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun getCreatedCalendar(id: String) {
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
    private fun updateStartDate(dateStart: LocalDate) {
        _state.update { currentState ->
            val updatedDateRange = DateRangeUi(dateStart, currentState.calendar?.days?.dateRange?.dateEnd ?: dateStart)
            val updatedDays = DaysUi(dateRange = updatedDateRange)

            currentState.copy(calendar = currentState.calendar?.copy(
                days = updatedDays
            ))
        }
    }

    private fun updateEndDate(dateEnd: LocalDate) {
        _state.update { currentState ->
            val updatedDateRange = DateRangeUi(currentState.calendar?.days?.dateRange?.dateStart ?: dateEnd, dateEnd)
            val updatedDays = DaysUi(dateRange = updatedDateRange)

            currentState.copy(calendar = currentState.calendar?.copy(
                days = updatedDays
            ))
        }
    }


    private fun updateRecipientEmail(email: String) {
        _state.update { it.copy(calendar = it.calendar?.copy(recipients = listOf(email))) }
    }

    private fun updateTitle(title: String) {
        _state.update { it.copy(calendar = it.calendar?.copy(title = title)) }
    }

    private fun selectCodeOption() {
        _state.update { it.copy(calendar = it.calendar?.copy(code = Random.nextInt(100000, 999999).toString(), recipients = emptyList())) }
    }

    private fun selectRecipientOption() {
        _state.update { it.copy(calendar = it.calendar?.copy(code = null, recipients = emptyList())) }
    }

    private fun updateIcon(icon: IconOptionUi) {
        _state.update { it.copy(calendar = it.calendar?.copy(icon = icon)) }
    }

    private fun updateDrawing(bitmap: Bitmap) {
        _state.update { it.copy(calendar = it.calendar?.copy(drawing = bitmap)) }
    }
}
