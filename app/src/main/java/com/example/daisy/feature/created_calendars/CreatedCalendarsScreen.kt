package com.example.daisy.feature.created_calendars

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.ui.util.UiEvent

@Composable
fun CreatedCalendarsScreen(
){
    CreatedCalendarsContent(
    )
}

@Composable
fun CreatedCalendarsContent(
    viewModel: CreatedCalendarsViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(CreatedCalendarsUserEvent.GetCreatedCalendars)
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

    }
    else{
        LazyColumn {
            items(state.calendars){
                Text(text = it.recipient.toString())
            }
        }
    }
}