package com.example.daisy.feature.calendars.created_calendars

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.feature.new_calendar.pages.NewCalendarDateForm
import com.example.daisy.feature.new_calendar.pages.NewCalendarPersonalizeForm
import com.example.daisy.feature.new_calendar.pages.NewCalendarRecipientForm
import com.example.daisy.ui.common.elements.TertiaryButton
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.IconOptionUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun CreatedCalendarEditorScreen(
    id: String?,
    viewModel: CreatedCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { id?.let { CreatedCalendarEditorUserEvent.GetCreatedCalendar(it) }
            ?.let { viewModel.onEvent(it) } },
    )

    CreatedCalendarEditorContent(
        state = state,
        onIconChange = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.IconChanged(it))
        },
        onTitleChange = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.TitleChanged(it))
        },
        onDateStartChange = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.StartChanged(it))
        },
        onDateEndChange = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.EndChanged(it))
        },
        onSaveModifications = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.SaveModifications)
        },
        onRecipientOptionSelected = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.RecipientOptionSelected)
        },
        onCodeOptionSelected = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.CodeOptionSelected)
        },
        onRecipientChange = {
            viewModel.onEvent(CreatedCalendarEditorUserEvent.RecipientEmailChanged(it))
        },
        onClose = {
            id?.let { CreatedCalendarEditorUserEvent.GetCreatedCalendar(it) }
                ?.let { viewModel.onEvent(it) }
        }
    )
}

@Composable
fun CreatedCalendarEditorContent(
    state: CreatedCalendarEditorUiState,
    onIconChange: (IconOptionUi) -> Unit,
    onTitleChange: (String) -> Unit,
    onDateStartChange: (LocalDate) -> Unit,
    onDateEndChange: (LocalDate) -> Unit,
    onSaveModifications: () -> Unit,
    onRecipientChange: (String) -> Unit,
    onRecipientOptionSelected: () -> Unit,
    onCodeOptionSelected: () -> Unit,
    onClose: () -> Unit
) {

    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        else -> CreatedCalendarEditor(
            calendarUi = state.calendar,
            onIconChange = onIconChange,
            onTitleChange = onTitleChange,
            onDateStartChange = onDateStartChange,
            onDateEndChange = onDateEndChange,
            onSaveModifications = onSaveModifications,
            onRecipientOptionSelected = onRecipientOptionSelected,
            onCodeOptionSelected = onCodeOptionSelected,
            onRecipientChange = onRecipientChange,
            onClose = onClose
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatedCalendarEditor(
    calendarUi: CalendarUi?,
    onTitleChange: (String) -> Unit,
    onIconChange: (IconOptionUi) -> Unit,
    onSaveModifications: () -> Unit,
    onDateEndChange: (LocalDate) -> Unit,
    onDateStartChange: (LocalDate) -> Unit,
    onRecipientChange: (String) -> Unit,
    onRecipientOptionSelected: () -> Unit,
    onCodeOptionSelected: () -> Unit,
    onClose: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }


    Column(
        Modifier.fillMaxSize()
    ) {
        CreatedCalendarEditorCard(
            calendarUi = calendarUi,
            interactionSource = interactionSource,
        )
        CreatedCalendarEditorDays(
            calendarUi = calendarUi
        )
    }
}

@Composable
fun CreatedCalendarEditorDays(
    calendarUi: CalendarUi?
) {
}

@Composable
fun CreatedCalendarEditorCard(
    calendarUi: CalendarUi?,
    interactionSource: MutableInteractionSource,
) {
    var isFlipped by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 0f else 360f,
        animationSpec = tween(durationMillis = 2000), label = "",
        finishedListener = {
            //isEditable = !isEditable
        }
    )

    Box(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(20.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        scope.launch {
                            delay(1000)
                            isEditable = isEditable.not()
                        }
                    },
                    onTap = {
                        isFlipped = isFlipped.not()
                    }
                )
            }
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 60 * density
            }
    ) {
        CalendarItemBackground(
            borderColor = Color.White.copy(0.1f),
            backgroundColor = MediumGrey,
            modifier = Modifier.height(220.dp)
        )

        calendarUi?.drawing?.let {
            Image(
                bitmap = it.asImageBitmap(),
                modifier = Modifier.alpha(Constants.CALENDAR_DRAWING_ALPHA),
                contentDescription = null
            )
        }

        calendarUi?.let {
            CalendarItemContent(
                calendarUi = it,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(20.dp)),
                type = Type.CREATED,
                isEditableOnLongClick = isEditable,
            )
        }
    }
}



@Composable
fun CreatedCalendarEditorItem(
    modifier: Modifier,
    item: CreatedCalendarEditorSteps
) {
    Row(
        modifier
            .padding(20.dp)
            .background(MediumGrey, RoundedCornerShape(20))
            .padding(10.dp)
    ) {
        Icon(imageVector = item.icon, contentDescription = null)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = item.title)
    }
}

@Composable
fun createdCalendarEditorSteps(): List<CreatedCalendarEditorSteps> = listOf(
    CreatedCalendarEditorSteps.Setup,
    CreatedCalendarEditorSteps.Content,
    CreatedCalendarEditorSteps.Dates,
    CreatedCalendarEditorSteps.Recipients,
    CreatedCalendarEditorSteps.Design,
)

sealed class CreatedCalendarEditorSteps(
    val title: String,
    val icon: ImageVector
) {
    data object Setup : CreatedCalendarEditorSteps("Setup", Icons.Default.NewReleases)
    data object Dates : CreatedCalendarEditorSteps("Dates", Icons.Default.DateRange)
    data object Content : CreatedCalendarEditorSteps("Content", Icons.Default.ContentCopy)
    data object Recipients : CreatedCalendarEditorSteps("Recipients", Icons.Default.SupervisorAccount)
    data object Design : CreatedCalendarEditorSteps("Design", Icons.Default.Draw)
}