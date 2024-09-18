package com.example.daisy.feature.calendars


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarsScreen
import com.example.daisy.feature.calendars.received_calendars.ReceivedCalendarsScreen

@Composable
fun CalendarsScreen(
    onNavigateToCreatedCalendar: (String) -> Unit
){
    CalendarsScreenContent(
        onNavigateToCreatedCalendar
    )
}

@Composable
fun CalendarsScreenContent(
    onNavigateToCreatedCalendar: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {2})

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> ReceivedCalendarsScreen()
            1 -> CreatedCalendarsScreen(onNavigateToCreatedCalendar)
        }
    }
}
