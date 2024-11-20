package com.example.daisy.feature.calendars.received_calendars

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.DayUi
import com.google.firebase.storage.FirebaseStorage

@Composable
fun ReceivedCalendarDayScreen(
    id: String?,
    number: Int?,
    viewModel: ReceivedCalendarDayViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (id != null && number != null) {
            viewModel.onEvent(ReceivedCalendarDayUserEvent.GetReceivedCalendarDay(id, number))
        }
    }

    val day = state.day
    val fileName = state.calendarId + state.day?.number.toString()
    Log.d("ReceivedCalendarDayScreen", "Retrieved day: $day")

    if (number != null && day != null) {
        ReceivedCalendarDayContent(
            state = state,
            day = day,
            fileName = fileName
        )
    }
}

@Composable
private fun ReceivedCalendarDayContent(
    state: ReceivedCalendarDayUiState,
    day: DayUi,
    fileName: String
) {
    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        else -> ReceivedCalendarDay(
            dayUi = day,
            fileName = fileName
        )
    }
}

@Composable
private fun ReceivedCalendarDay(
    dayUi: DayUi,
    fileName: String
) {
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    Column(modifier = Modifier.padding(bottom = 50.dp)) {
        Text(
            text = dayUi.message,
            modifier = Modifier.padding(16.dp)
        )
        ImageViewer(
            fileName = fileName,
            imageUri = imageUri.value,
            onImageLoaded = {
                imageUri.value = it
            }
        )
    }
}

@Composable
private fun ImageViewer(
    fileName: String,
    imageUri: Uri?,
    onImageLoaded: (Uri?) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("calendarImages/$fileName.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { url ->
                onImageLoaded(url)
            }
            .addOnFailureListener {
                //Toast.makeText(context, "Failed to load image: $fileName", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        SubcomposeAsyncImage(
            loading = {
                Box(modifier = Modifier.padding(20.dp)) {
                    LoadingContent()
                }
            },
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 20.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}
