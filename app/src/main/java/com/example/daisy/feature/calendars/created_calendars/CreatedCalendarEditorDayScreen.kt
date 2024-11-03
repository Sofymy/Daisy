package com.example.daisy.feature.calendars.created_calendars

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.daisy.ui.common.elements.PrimaryButton
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.common.elements.SecondaryButton
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.DayUi
import com.google.firebase.storage.FirebaseStorage
import androidx.activity.compose.rememberLauncherForActivityResult as rememberLauncher

@Composable
fun CreatedCalendarEditorDayScreen(
    id: String?,
    number: Int?,
    viewModel: CreatedCalendarEditorDayViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { if (id != null && number != null) viewModel.onEvent(CreatedCalendarEditorDayUserEvent.GetCreatedCalendarDay(id, number)) }
    )

    val day = state.day
    val fileName = state.calendarId + state.day?.number.toString()
    Log.d("CreatedCalendarEditorDayScreen", "Retrieved day: $day")

    if (number != null && day != null) {
        CreatedCalendarEditorDayContent(
            state = state,
            day = day,
            fileName = fileName,
            onMessageChange = {
                viewModel.onEvent(CreatedCalendarEditorDayUserEvent.MessageChanged(it)) },
            onSaveModifications = { uri ->
                viewModel.onEvent(CreatedCalendarEditorDayUserEvent.SaveModifications(number, id))
                if (uri != null && !uri.toString().startsWith("https")) {
                    uploadImage(uri, context, fileName)
                } else {
                    Log.d("CreatedCalendarEditorDayScreen", "No image selected, only message is being saved")
                }
            }
        )
    }
}

@Composable
private fun CreatedCalendarEditorDayContent(
    state: CreatedCalendarEditorDayUiState,
    day: DayUi,
    onMessageChange: (String) -> Unit,
    onSaveModifications: (Uri?) -> Unit,
    fileName: String
) {
    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        else -> CreatedCalendarEditorDay(
            dayUi = day,
            fileName = fileName,
            onMessageChange = onMessageChange,
            onSaveModifications = onSaveModifications
        )
    }
}

@Composable
private fun CreatedCalendarEditorDay(
    dayUi: DayUi,
    onMessageChange: (String) -> Unit,
    onSaveModifications: (Uri?) -> Unit,
    fileName: String
) {
    val isImageSet = false
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Column(modifier = Modifier.padding(bottom = 50.dp)) {
        PrimaryTextField(
            value = dayUi.message,
            onValueChange = { onMessageChange(it) },
            icon = Icons.AutoMirrored.Filled.Message
        )
        ImagePicker(
            modifier = Modifier.weight(1f),
            onImagePicked = { imageUri = it },
            fileName = fileName,
            imageUri = imageUri
        )
        SecondaryButton(
            onClick = {
                if (imageUri != null) {
                    onSaveModifications(imageUri)
                } else {
                    onSaveModifications(null)
                }
            },
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
private fun ImagePicker(
    modifier: Modifier,
    onImagePicked: (Uri?) -> Unit,
    fileName: String,
    imageUri: Uri?
) {
    val launcher = rememberLauncher(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        onImagePicked(uri)
    }

    val context = LocalContext.current

    LaunchedEffect(fileName) {
        val storageRef = FirebaseStorage.getInstance().reference.child("calendarImages/$fileName.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { url ->
                onImagePicked(url)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load image: $fileName", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        SubcomposeAsyncImage(
            loading = {
                Box(modifier = modifier) {
                    LoadingContent()
                }
            },
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentScale = ContentScale.FillWidth
        )
        PrimaryButton(
            onClick = { launcher.launch(arrayOf("image/*")) },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Pick Image")
        }
    }
}


private fun uploadImage(uri: Uri?, context: Context, fileName: String) {
    uri?.let {
        val storageRef = FirebaseStorage.getInstance().reference.child("calendarImages/$fileName.jpg")

        storageRef.putFile(it)
            .addOnSuccessListener {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
