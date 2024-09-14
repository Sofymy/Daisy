package com.example.daisy.feature.created_calendars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi

@Composable
fun CreatedCalendarScreen(
    id: String?,
    viewModel: CreatedCalendarViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { id?.let { CreatedCalendarUserEvent.GetCreatedCalendar(it) }
            ?.let { viewModel.onEvent(it) } },
    )

    CreatedCalendarContent(state = state)
}

@Composable
fun CreatedCalendarContent(
    state: CreatedCalendarUiState
) {

    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        else -> CreatedCalendarItem(calendarUi = state.calendar)
    }
}

@Composable
fun CreatedCalendarItem(
    calendarUi: CalendarUi?
) {
    calendarUi?.let {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .background(Color.White)
                .fillMaxWidth()
        ) {
            Text(text = it.id.toString())
            Text(text = it.dateRange.dateStart.toString())
        }
    } ?: run {
        Text("No calendar data available.")
    }
}
