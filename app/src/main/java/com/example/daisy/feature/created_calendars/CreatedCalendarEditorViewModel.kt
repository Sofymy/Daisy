package com.example.daisy.feature.created_calendars

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


data class CreatedCalendarEditorUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val calendar: CalendarUi? = CalendarUi()
)

sealed class CreatedCalendarEditorUserEvent {
    data class GetCreatedCalendar(val id: String) : CreatedCalendarEditorUserEvent()
}


@HiltViewModel
class CreatedCalendarViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {

    private var _state = MutableStateFlow(CreatedCalendarEditorUiState())
    var state = _state

    fun onEvent(event: CreatedCalendarEditorUserEvent) {
        when(event) {
            is CreatedCalendarEditorUserEvent.GetCreatedCalendar -> {
                getCreatedCalendar(id = event.id)
            }
        }
    }

    private fun getCreatedCalendar(id: String) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val calendar = calendarUseCases.getCreatedCalendarUseCase(id).getOrThrow()?.toUi()
                    _state.update { it.copy(
                        isLoading = false,
                        calendar = calendar
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