package com.example.daisy.feature.create_calendar.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.create_calendar.CreateCalendarUiState
import com.example.daisy.feature.create_calendar.CreateCalendarUserEvent
import com.example.daisy.feature.create_calendar.CreateCalendarViewModel

@Composable
fun CreateCalendarRecipient() {
    CreateCalendarRecipientContent()
}

@Composable
fun CreateCalendarRecipientContent(
    viewModel: CreateCalendarViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateCalendarRecipientForm(
        state = state,
        onFieldChange = { viewModel.onEvent(it) },
        onClickNext = {  }
    )

}

@Composable
fun CreateCalendarRecipientForm(
    state: CreateCalendarUiState,
    onFieldChange: (CreateCalendarUserEvent) -> Unit,
    onClickNext: () -> Unit
) {


    Column {

        TextField(value = state.recipient.name, onValueChange = {
            onFieldChange(CreateCalendarUserEvent.RecipientNameChanged(it))
        })

        Button(
            onClick = { onClickNext() },
            enabled = state.recipient.name.isNotBlank()
        ) {
            Text("Next")
        }

    }

}
