package com.example.daisy.feature.calendars.received_calendars


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
import androidx.compose.ui.text.font.FontWeight
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
import com.example.daisy.ui.model.DayUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun ReceivedCalendarScreen(
    id: String?,
    onNavigateToReceivedCalendarDay: (Int) -> Unit,
    viewModel: ReceivedCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { id?.let { ReceivedCalendarUserEvent.GetReceivedCalendar(it) }
            ?.let { viewModel.onEvent(it) } },
    )

    ReceivedCalendarContent(
        state = state,
        onClickDay = { onNavigateToReceivedCalendarDay(it) }
    )
}

@Composable
fun ReceivedCalendarContent(
    state: ReceivedCalendarUiState,
    onClickDay: (Int) -> Unit
) {
    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        state.calendar != null -> {
            ReceivedCalendar(
                calendarUi = state.calendar,
                onClickDay = onClickDay
            )
        }
    }
}

@Composable
fun ReceivedCalendar(
    calendarUi: CalendarUi,
    onClickDay: (Int) -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        item {
            ReceivedCalendarCard(
                calendarUi = calendarUi,
                interactionSource = interactionSource,
            )
        }
        item {
            ReceivedCalendarDays(
                calendarUi = calendarUi,
                onClickDay = onClickDay
            )
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ReceivedCalendarDays(
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
                    ReceivedCalendarDayItem(
                        day = it,
                        modifier = Modifier.weight(1f),
                        onClickDay = onClickDay
                    )
                }
                if (it.size < 3) {
                    Spacer(modifier = Modifier.weight(1f))
                    if (it.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ReceivedCalendarDayItem(
    day: DayUi,
    modifier: Modifier,
    onClickDay: (Int) -> Unit
) {
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
                .background(MediumGrey, RoundedCornerShape(10.dp))
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
fun ReceivedCalendarCard(
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
            // isEditable = !isEditable
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
            calendarUi = calendarUi,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp)),
            type = Type.RECEIVED,
            isEditableOnLongClick = isEditable,
        )
    }
}
