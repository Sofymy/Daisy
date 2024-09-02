package com.example.daisy.feature.create_calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCalendarViewModel @Inject constructor(
    private val createCalendarUseCases: AuthUseCases
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent<Nothing>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var _state = MutableStateFlow(CreateCalendarUiState())
    var state = _state

    fun onEvent(event: CreateCalendarUserEvent) {
        when(event) {
            is CreateCalendarUserEvent.StartChanged -> {
                _state.update { it.copy(dateRange = it.dateRange.copy(dateStart = event.dateStart)) }
            }
            is CreateCalendarUserEvent.EndChanged -> {
                _state.update { it.copy(dateRange = it.dateRange.copy(dateEnd = event.dateEnd)) }
            }

            is CreateCalendarUserEvent.RecipientNameChanged -> {
                _state.update { it.copy(recipient = it.recipient.copy(name = event.recipientName)) }
            }
        }
    }

    private fun createCalendar() = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("eeeeeeeee", state.value.toString())
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

}