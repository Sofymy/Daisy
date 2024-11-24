package com.example.daisy.feature.calendars.created_calendars

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.DayUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CreatedCalendarEditorScreen(
    id: String?,
    onNavigateToCreatedCalendarEditorDay: (Int) -> Unit,
    viewModel: CreatedCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { id?.let { CreatedCalendarEditorUserEvent.GetCreatedCalendar(it) }
            ?.let { viewModel.onEvent(it) } },
    )

    CreatedCalendarEditorContent(
        state = state,
        onClickDay = { onNavigateToCreatedCalendarEditorDay(it) }
    )
}

@Composable
fun CreatedCalendarEditorContent(
    state: CreatedCalendarEditorUiState,
    onClickDay: (Int) -> Unit
) {

    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        state.calendar != null -> {
            CreatedCalendarEditor(
                calendarUi = state.calendar,
                onClickDay = onClickDay
            )
        }
    }
}

@Composable
fun CreatedCalendarEditor(
    calendarUi: CalendarUi,
    onClickDay: (Int) -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }


    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        if(calendarUi.code?.isNotEmpty() == true){
            item {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.code, calendarUi.code),
                        modifier = Modifier.padding(vertical = 5.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        item {
            CreatedCalendarEditorCard(
                calendarUi = calendarUi,
                interactionSource = interactionSource,
            )
        }
        item{
            CreatedCalendarEditorDays(
                calendarUi = calendarUi,
                onClickDay = onClickDay
            )
        }
        item{
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun CreatedCalendarEditorDays(
    calendarUi: CalendarUi,
    onClickDay: (Int) -> Unit
) {
    val days = calendarUi.days.calculateDays()

    Column(
        Modifier.padding(20.dp)
    ) {
        days.chunked(3).forEach {
            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                it.forEach {
                    CreatedCalendarEditorDayItem(
                        day = it,
                        modifier = Modifier.weight(1f),
                        onClickDay = onClickDay
                    )
                }
                if(it.size < 3){
                    Spacer(modifier = Modifier.weight(1f))
                    if(it.size < 2){
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun CreatedCalendarEditorDayItem(
    day: DayUi,
    modifier: Modifier,
    onClickDay: (Int) -> Unit
) {
    val isLocked = day.date.atStartOfDay() >= LocalDate.now().plusDays(1).atStartOfDay()

    Box(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onClickDay(day.number)
                }
                .fillMaxSize()
                .border(1.dp, Color.White.copy(.1f), RoundedCornerShape(10.dp))
                .background(if (isLocked) MediumGrey else Purple, RoundedCornerShape(10.dp))
        ) {
            Icon(imageVector = Icons.Filled.Window, contentDescription = null, Modifier.fillMaxSize(), tint = Color.White.copy(.03f))
            Text(
                text = day.number.toString(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }
        Text(
            text = day.date.format(DateTimeFormatter.ofPattern("MM.dd.")),
            modifier = Modifier
                .offset(y = 10.dp)
                .border(1.dp, Color.White.copy(.4f), CutCornerShape(6.dp))
                .background(Color.DarkGray, CutCornerShape(6.dp))
                .padding(2.dp)
        )
    }
}



@Composable
fun CreatedCalendarEditorCard(
    calendarUi: CalendarUi,
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
            .padding(20.dp)
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

        calendarUi.drawing?.let {
            Image(
                bitmap = it.asImageBitmap(),
                modifier = Modifier.alpha(Constants.CALENDAR_DRAWING_ALPHA),
                contentDescription = null
            )
        }

        CalendarItemContent(
            type = Type.CREATED,
            calendarUi = calendarUi,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp)),
            isEditableOnLongClick = isEditable,
        )
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