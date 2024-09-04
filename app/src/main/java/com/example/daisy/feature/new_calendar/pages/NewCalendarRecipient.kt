package com.example.daisy.feature.new_calendar.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.model.CalendarUi

@Composable
fun NewCalendarRecipient(
    onClickCreate: () -> Unit
) {
    NewCalendarRecipientContent(
        onClickCreate = onClickCreate
    )
}

@Composable
fun NewCalendarRecipientContent(
    viewModel: NewCalendarViewModel = hiltViewModel(),
    onClickCreate: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewCalendarRecipientContentForm(
        state = state,
        onFieldChange = { viewModel.onEvent(it) },
        onClickCreate = onClickCreate
    )

}

@Composable
fun NewCalendarRecipientContentForm(
    state: CalendarUi,
    onFieldChange: (NewCalendarUserEvent) -> Unit,
    onClickCreate: () -> Unit
) {


    Column {

        TextField(value = state.recipient.name, onValueChange = {
            onFieldChange(NewCalendarUserEvent.RecipientNameChanged(it))
        })

        TextField(value = state.recipient.email, onValueChange = {
            onFieldChange(NewCalendarUserEvent.RecipientEmailChanged(it))
        })

        Button(
            onClick = onClickCreate,
            enabled = state.recipient.name.isNotBlank()
        ) {
            Text("Next")
        }

    }

}
