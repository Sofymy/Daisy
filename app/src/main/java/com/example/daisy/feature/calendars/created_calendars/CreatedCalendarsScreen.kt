package com.example.daisy.feature.calendars.created_calendars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.util.Constants

@Composable
fun CreatedCalendarsScreen(
    onNavigateToCreatedCalendar: (String) -> Unit,
    searchExpression: String
) {
    CreatedCalendarsContent(
        onNavigateToCreatedCalendar = onNavigateToCreatedCalendar,
        searchExpression = searchExpression
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreatedCalendarsContent(
    viewModel: CreatedCalendarsViewModel = hiltViewModel(),
    onNavigateToCreatedCalendar: (String) -> Unit,
    searchExpression: String,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.onEvent(CreatedCalendarsUserEvent.RefreshCreatedCalendars) }
    )

    HandleLifecycleEvents(
        onResume = { viewModel.onEvent(CreatedCalendarsUserEvent.GetCreatedCalendars) }
    )

    when {
        state.isLoading -> LoadingContent()
        state.isError -> ErrorContent()
        else -> {
            Box(
                Modifier.pullRefresh(pullRefreshState)
            ) {
                CreatedCalendarsList(
                    calendars = state.calendars.filter {
                        it.toString().contains(searchExpression, ignoreCase = true)
                    },
                    onNavigateToCreatedCalendar = onNavigateToCreatedCalendar
                )
                PullRefreshIndicator(
                    refreshing = state.isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = if (state.isRefreshing) Purple else MediumGrey,
                    contentColor = Color.White
                )
            }

        }
    }
}

@Composable
fun CreatedCalendarsList(
    calendars: List<CalendarUi>,
    onNavigateToCreatedCalendar: (String) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(25.dp))
        }
        items(
            items = calendars.sortedBy { it.dateRange.dateStart }
        ) { calendar ->
            CreatedCalendarItem(
                calendarUi = calendar,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun CreatedCalendarItem(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        CalendarItemBackground(
            borderColor = Color.White.copy(0.1f),
            backgroundColor = MediumGrey,
            modifier = Modifier.height(220.dp)
        )
        calendarUi.drawing?.let {
            Image(bitmap = it.asImageBitmap(),
                modifier = Modifier
                    .alpha(Constants.CALENDAR_DRAWING_ALPHA),
                contentDescription = null)
        }
        CalendarItemContent(
            calendarUi = calendarUi,
            modifier = Modifier.matchParentSize(),
            type = Type.CREATED
            //onClickItem = {  }
        )
    }
}