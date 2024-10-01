package com.example.daisy.feature.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.util.Constants
import kotlinx.coroutines.delay


@Composable
fun HomeReceivedCalendars(
    receivedCalendars: List<CalendarUi>,
    navigateToReceivedCalendars: () -> Unit
) {
    val activeCalendar = remember {
        mutableIntStateOf(0)
    }
    val interactionSource = remember { MutableInteractionSource() }

    val isDragged by interactionSource.collectIsDraggedAsState()
    val numberOfReceivedCalendars = remember {
        mutableIntStateOf(receivedCalendars.size)
    }
    val dragDirection = remember {
        mutableStateOf(Direction.RIGHT)
    }

    LaunchedEffect(receivedCalendars) {
        numberOfReceivedCalendars.intValue = receivedCalendars.size
        activeCalendar.intValue = 0
    }

    LaunchedEffect(isDragged) {
        if(isDragged) {
            when(dragDirection.value){
                Direction.LEFT -> {
                    if (activeCalendar.intValue >= numberOfReceivedCalendars.intValue - 1) {
                        activeCalendar.intValue = 0
                    } else {
                        activeCalendar.intValue += 1
                    }
                }
                Direction.RIGHT -> {
                    if (activeCalendar.intValue <= 0) {
                        activeCalendar.intValue = numberOfReceivedCalendars.intValue - 1
                    } else {
                        activeCalendar.intValue -= 1
                    }
                }
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "Received calendars", fontWeight = FontWeight.Bold)
        IconButton(onClick = {
            navigateToReceivedCalendars()
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(20.dp))
        }
    }
    Spacer(modifier = Modifier.height(30.dp))

    HomeReceivedCalendarsBoxes(
        receivedCalendars = receivedCalendars,
        interactionSource = interactionSource,
        activeCalendar = activeCalendar.intValue,
        isDragged = isDragged,
        onChangeDragDirection = { dragDirection.value = it },
        numberOfReceivedCalendars = numberOfReceivedCalendars.intValue
    )

    HomeReceivedCalendarsDots(
        numberOfReceivedCalendars = numberOfReceivedCalendars.intValue,
        activeCalendar = activeCalendar.intValue
    )
}


@Composable
fun HomeReceivedCalendarBox(
    i: Int,
    isDragged: Boolean
) {
    val startAnimation = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isDragged) {
        if(isDragged){
            startAnimation.value = true
        }
        delay(300)
        startAnimation.value = false
    }

    val contentSize = animateFloatAsState(targetValue = if(startAnimation.value) 1f else (8 - i) / 8.toFloat(), animationSpec = tween(300),
        label = ""
    )
    val offsetSize = animateDpAsState(targetValue = if(startAnimation.value) 0.dp else 20.dp * i / 2f, animationSpec = tween(300),
        label = ""
    )
    Box(
        Modifier
            .graphicsLayer {
                scaleX = contentSize.value
            }
            .offset { IntOffset(x = 0, y = offsetSize.value.roundToPx()) }
            .clip(RoundedCornerShape(20.dp))
            .alpha((5 - i) / 5.toFloat())
            .border(1.dp, Color.White.copy(0.3f), RoundedCornerShape(20.dp))
            .background(Purple)
            .fillMaxWidth()
            .height(220.dp)
            .blur(20.dp)
            .drawBehind {
                drawCircle(Color.Black.copy(.2f), center = Offset(150f, 150f), radius = 450f)
            }
            .padding(20.dp)
    )
}

enum class Direction{
    LEFT, RIGHT
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeReceivedCalendarsBoxes(
    interactionSource: MutableInteractionSource,
    isDragged: Boolean,
    activeCalendar: Int,
    numberOfReceivedCalendars: Int,
    onChangeDragDirection: (Direction) -> Unit,
    receivedCalendars: List<CalendarUi>
) {
    val dragState = rememberDraggable2DState(onDelta = { delta ->
        onChangeDragDirection(if (delta.x <= 0) Direction.LEFT else Direction.RIGHT)
    })

    Box(
        modifier = Modifier
            .draggable2D(
                startDragImmediately = false,
                state = dragState,
                interactionSource = interactionSource
            )
            .padding(horizontal = 20.dp)
    ) {

        val displayedCalendars = (numberOfReceivedCalendars.coerceIn(0, 3) - 1 downTo 0)

        for (i in displayedCalendars) {
            HomeReceivedCalendarBox(i, isDragged)
        }

        receivedCalendars.getOrNull(activeCalendar)?.let { activeCalendarUi ->
            activeCalendarUi.drawing?.let {
                Image(bitmap = it.asImageBitmap(),
                    modifier = Modifier
                        .alpha(Constants.CALENDAR_DRAWING_ALPHA),
                    contentDescription = null)
            }
            CalendarItemContent(calendarUi = activeCalendarUi, type = Type.RECEIVED, modifier = Modifier.matchParentSize())
        }
    }
}


@Composable
fun HomeReceivedCalendarsDots(
    numberOfReceivedCalendars: Int,
    activeCalendar: Int
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for(i in (0..<numberOfReceivedCalendars)){

            val color = if (activeCalendar == i) Color.LightGray else Color.DarkGray
            val size = animateDpAsState(targetValue = if (activeCalendar == i) 20.dp else 10.dp,
                label = ""
            )

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .width(size.value)
                    .background(color)
                    .height(10.dp)
            )
        }
    }
}
