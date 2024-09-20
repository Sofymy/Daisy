package com.example.daisy.feature.calendars.created_calendars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumGrey

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
    calendars: List<CalendarUi>,
    onNavigateToCreatedCalendar: (String) -> Unit
) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(25.dp))
        }
        items(
            items = calendars.sortedBy { it.dateRange.dateStart }
        ) { calendar ->
            CreatedCalendarItem(
                calendarUi = calendar,
            )
        }
    }
}

@Composable
fun CreatedCalendarItem(
    calendarUi: CalendarUi
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        CalendarItemBackground(
            borderColor = Color.White.copy(0.1f),
            backgroundColor = MediumGrey
        )
        CalendarItemContent(
            calendarUi = calendarUi,
            modifier = Modifier.matchParentSize()
            //onClickItem = {  }
        )
    }
}