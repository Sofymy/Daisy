package com.example.daisy.feature.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import kotlinx.coroutines.delay
import java.time.Duration.between
import java.time.LocalDate


@Composable
fun HomeReceivedCalendars(
    receivedCalendars: List<CalendarUi>
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
        Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
        Text(text = "Received calendars", fontWeight = FontWeight.Bold)
        Text(text = "View all")
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
    Box(
        Modifier
            .draggable2D(
                startDragImmediately = false,
                state = rememberDraggable2DState(onDelta = {
                    if (it.x <= 0) {
                        onChangeDragDirection(Direction.LEFT)
                    } else
                        onChangeDragDirection(Direction.RIGHT)
                }),
                interactionSource = interactionSource
            )
            .padding(horizontal = 20.dp)
    ) {
        for(i in (numberOfReceivedCalendars.coerceIn(0,3)-1 downTo 0)){
            HomeReceivedCalendarBox(i, isDragged)
        }
        if(receivedCalendars.isNotEmpty()) {
            HomeReceivedCalendarBoxContent(receivedCalendars[activeCalendar], modifier = Modifier.matchParentSize())
        }
    }
}

@Composable
fun HomeReceivedCalendarBoxContent(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    var days by remember { mutableIntStateOf(0) }

    val daysCounter by animateIntAsState(
        targetValue = days,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotateLock = infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = ""
    )

    LaunchedEffect(Unit) {
        days = 24
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        HomeReceivedCalendarBoxContentSender(calendarUi, Modifier.align(Alignment.TopStart))
        HomeReceivedCalendarBoxContentDayCounter(daysCounter, Modifier.align(Alignment.TopEnd))
        HomeReceivedCalendarBoxContentLockButton(rotateLock.value, Modifier.align(Alignment.BottomEnd))
        HomeReceivedCalendarBoxContentOpenText(calendarUi, Modifier.align(Alignment.BottomStart))
    }
}

@Composable
fun HomeReceivedCalendarBoxContentSender(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    Box(
        modifier
            .padding(top = 20.dp, start = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = null,
                Modifier
                    .border(1.dp, Color.White.copy(.3f), CircleShape)
                    .size(50.dp)
                    .background(Color.White.copy(.2f), CircleShape)
                    .padding(13.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = calendarUi.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "From ${calendarUi.sender.name}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun HomeReceivedCalendarBoxContentDayCounter(
    daysCounter: Int,
    modifier: Modifier
) {
    Box(
        modifier
            .padding(20.dp)
            .border(1.dp, Color.White.copy(.3f), CutCornerShape(10.dp))
            .clip(CutCornerShape(10.dp))
            .background(Color.White.copy(.2f))
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = daysCounter.toString(),
                color = Color.White.copy(.5f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = "days", color = Color.White)
        }
    }
}

@Composable
fun HomeReceivedCalendarBoxContentLockButton(
    rotateLock: Float,
    modifier: Modifier
) {
    Box(
        modifier
            .padding(20.dp)
            .wrapContentSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            modifier = Modifier,
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = MediumGrey)
        ) {
            Text(text = "Closed", color = Color.White.copy(0.3f))
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotateLock
                },
                tint = Color.White.copy(.3f)
            )
        }
    }
}

@Composable
fun HomeReceivedCalendarBoxContentOpenText(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    Box(
        modifier
            .padding(20.dp)
            .wrapContentSize()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Opens in ${between(LocalDate.now().atStartOfDay(), calendarUi.dateRange.dateStart.atStartOfDay()).toDays()} days",
                fontWeight = FontWeight.Bold
            )
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
