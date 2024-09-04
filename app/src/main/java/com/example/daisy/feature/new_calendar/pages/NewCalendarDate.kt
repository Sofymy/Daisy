package com.example.daisy.feature.new_calendar.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.model.CalendarUi
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun NewCalendarDate(
    onClickNext: () -> Unit
) {
    NewCalendarDateContent(onClickNext = onClickNext)
}

@Composable
fun NewCalendarDateContent(
    viewModel: NewCalendarViewModel = hiltViewModel(),
    onClickNext: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewCalendarDateForm(
        state = state,
        onFieldChange = { viewModel.onEvent(it) },
        onClickNext = onClickNext
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalendarDateForm(
    state: CalendarUi,
    onFieldChange: (NewCalendarUserEvent) -> Unit,
    onClickNext: () -> Unit
) {

    val dateRangeState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Picker)
    val isCalendarValueSet = remember(dateRangeState.selectedStartDateMillis, dateRangeState.selectedEndDateMillis) {
        mutableStateOf(dateRangeState.selectedStartDateMillis != null && dateRangeState.selectedEndDateMillis != null)
    }
    
    LaunchedEffect(dateRangeState.selectedEndDateMillis, dateRangeState.selectedStartDateMillis) {
        onFieldChange(
            NewCalendarUserEvent.StartChanged(
                convertMillisToLocalDate(
                    dateRangeState.selectedStartDateMillis ?: 0
                )
            )
        )
        onFieldChange(
            NewCalendarUserEvent.EndChanged(
                convertMillisToLocalDate(
                    dateRangeState.selectedEndDateMillis ?: 0
                )
            )
        )
    }

    Column {

        AnimatedVisibility(visible = isCalendarValueSet.value) {
            Text(
                text = "Calendar for ${
                    Duration.between(
                        state.dateRange.dateStart.atStartOfDay(),
                        state.dateRange.dateEnd.atStartOfDay()
                    ).toDays() + 1
                } days"
            )
        }
        DateRangePicker(
            state = dateRangeState,
            showModeToggle = false,
            modifier = Modifier.fillMaxHeight(0.7f)
        )

        Button(
            onClick = onClickNext,
            enabled = isCalendarValueSet.value
        ) {
            Text("Next")
        }

    }

}

fun convertMillisToLocalDate(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    val instant = Instant.ofEpochMilli(millis)
    return instant.atZone(zoneId).toLocalDate()
}