package com.example.daisy.feature.new_calendar.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.RecipientOption

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
    val options = RecipientOption.entries.toTypedArray()
    val selectedValue = remember { mutableStateOf(RecipientOption.EMAIL) }

    val isSelectedItem: (RecipientOption) -> Boolean = { selectedValue.value == it }
    val onChangeState: (RecipientOption) -> Unit = { selectedValue.value = it }

    options.forEach { item ->
        Column(
            modifier = Modifier.selectable(
                selected = isSelectedItem(item),
                onClick = {
                    onChangeState(item)
                    when(item){
                        RecipientOption.EMAIL -> {
                            onFieldChange(NewCalendarUserEvent.RecipientOptionSelected)
                        }
                        RecipientOption.CODE -> {
                            onFieldChange(NewCalendarUserEvent.CodeOptionSelected)
                        }
                    } },
            )
        ) {
            Text(
                text = item.name,
                modifier = Modifier
                    .background(if(isSelectedItem(item)) Color.Green else Color.Transparent)
                    .fillMaxWidth()
            )
            if(isSelectedItem(item)){
                when(selectedValue.value){
                    RecipientOption.EMAIL -> {

                        Column {
                            TextField(value = state.recipients.firstNotNullOf { it }, onValueChange = { it2->
                                onFieldChange(NewCalendarUserEvent.RecipientEmailChanged(it2))
                            })

                        }
                    }
                    RecipientOption.CODE -> {
                        onFieldChange(NewCalendarUserEvent.CodeOptionSelected)
                    }
                }
            }
        }
    }

    Button(
        onClick = onClickCreate,
    ) {
        Text("Next")
    }

}
