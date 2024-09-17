package com.example.daisy.feature.created_calendars

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun CreatedCalendarsScreen(
    onNavigateToCreatedCalendar: (String) -> Unit
) {
    CreatedCalendarsContent(
        onNavigateToCreatedCalendar = onNavigateToCreatedCalendar
    )
}

@Composable
fun CreatedCalendarsContent(
    viewModel: CreatedCalendarsViewModel = hiltViewModel(),
    onNavigateToCreatedCalendar: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { viewModel.onEvent(CreatedCalendarsUserEvent.GetCreatedCalendars) }
    )

    Log.d("eeeeeeee", state.toString())
    when {
        state.isLoading -> LoadingContent()
        state.isError -> ErrorContent()
        else -> {
            CalendarsList(
                calendars = state.calendars,
                onNavigateToCreatedCalendar = onNavigateToCreatedCalendar
            )
        }
    }
}

@Composable
fun CalendarsList(
    calendars: List<CalendarUi?>,
    onNavigateToCreatedCalendar: (String) -> Unit
) {
    LazyColumn {
        items(calendars) { calendar ->
            if (calendar != null) {
                CreatedCalendarItem(
                    calendarUi = calendar,
                    onClickItem = onNavigateToCreatedCalendar
                )
            }
        }
    }
}

@Composable
fun CreatedCalendarItem(
    calendarUi: CalendarUi,
    onClickItem: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color.Transparent)
            .clickable { onClickItem(calendarUi.id) }
            .padding(20.dp)
    ) {
        Text(text = calendarUi.id)
        Text(text = calendarUi.recipients.toString())
        Text(text = calendarUi.dateRange.dateStart.toString())
    }
}