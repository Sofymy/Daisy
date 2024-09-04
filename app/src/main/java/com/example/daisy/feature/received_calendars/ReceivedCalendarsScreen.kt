package com.example.daisy.feature.received_calendars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.ui.model.CalendarUi

@Composable
fun ReceivedCalendarsScreen(
){
    ReceivedCalendarsContent()
}

@Composable
fun ReceivedCalendarsContent(
    viewModel: ReceivedCalendarsViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(ReceivedCalendarsUserEvent.GetReceivedCalendars)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if(state.isError){
        Text("Error")
    }
    else if(state.isLoading){
        CircularProgressIndicator()
    }
    else{
        LazyColumn() {
            items(
                items = state.calendars,
                key = { it.id }
            ){
                ReceivedCalendarItem(it)
            }
        }
    }
}

@Composable
fun ReceivedCalendarItem(
    calendarUi: CalendarUi
) {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
    ) {
        Text(text = calendarUi.id.toString())
        Text(text = calendarUi.sender.name)
        Text(text = calendarUi.dateRange.dateStart.toString())
    }
}