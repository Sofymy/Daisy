package com.example.daisy.feature.home

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daisy.R
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.DarkBlue
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.DarkPurple
import com.example.daisy.ui.theme.LightBlue
import com.example.daisy.ui.theme.LightPurple
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import java.time.Duration.between
import java.time.LocalDate


@Composable
fun HomeScreen(
    onNavigateToNewCalendar: () -> Unit,
    onNavigateToCreatedCalendars: () -> Unit
) {
    HomeScreenContent()
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = {
            viewModel.onEvent(HomeUserEvent.GetCurrentUser)
            viewModel.onEvent(HomeUserEvent.GetCreatedCalendars)
            viewModel.onEvent(HomeUserEvent.GetReceivedCalendars)
        }
    )

    when {
        state.isLoading -> LoadingContent()
        else -> {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        contentAlignment = Alignment.TopCenter
                    ) {
                        HomeAuroraAnimation()
                        HomeHeader(state.currentUser)
                    }
                }
                item {
                    HomeReceivedCalendars(state.receivedCalendars)
                }
                item {
                    HomeCreatedCalendars(state.createdCalendars)
                }
            }
        }
    }
}


@Composable
fun HomeCreatedCalendars(
    createdCalendars: List<CalendarUi>
) {
    val numberOfReceivedCalendars = remember {
        mutableIntStateOf(createdCalendars.size)
    }
    val activeCalendar = remember {
        mutableIntStateOf(0)
    }
    val state = rememberLazyListState()
    val colors = listOf(DarkBlue, DarkPurple, LightBlue, DarkBlue)

    LaunchedEffect(createdCalendars) {
        numberOfReceivedCalendars.intValue = createdCalendars.size
        activeCalendar.intValue = 0
    }
    
    Spacer(modifier = Modifier.height(30.dp))
    Row(
        Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
        Text(text = "My calendars", fontWeight = FontWeight.Bold)
        Text(text = "View all")
    }
    Spacer(modifier = Modifier.height(20.dp))
    LazyRow(
        state = state,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { Spacer(modifier = Modifier.width(20.dp)) }
        itemsIndexed(
            items = createdCalendars,
        ){index, calendarUi ->
            val firstIndex = remember {
                derivedStateOf { state.firstVisibleItemIndex == index }
            }
            val size = animateFloatAsState(targetValue = if(firstIndex.value) 1.7f else 1.5f,
                animationSpec = tween(600),
                label = ""
            )
            Box(contentAlignment = Alignment.BottomCenter){
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White.copy(.1f), RoundedCornerShape(10))
                        .clip(RoundedCornerShape(10))
                        .background(MediumGrey)
                        .size(size = (size.value * 100).dp),
                    contentAlignment = Alignment.TopCenter
                ){
                    HomeCreatedCalendarContent(calendarUi)
                }
                Box(modifier = Modifier
                    .offset(y = 10.dp)
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-10).dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp)
                    ) {
                        calendarUi.recipients.forEachIndexed { i, it ->
                            Box(modifier = Modifier
                                .border(1.dp, Color.White.copy(.1f), CircleShape)
                                .clip(CircleShape)
                                .background(colors[i])
                                .size(40.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = (it[0]).toString())
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(20.dp))
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun HomeCreatedCalendarContent(
    calendarUi: CalendarUi,
) {
    Column {
        HomeCreatedCalendarBoxContentRecipient(calendarUi = calendarUi, modifier = Modifier)
    }
}


@Composable
fun HomeCreatedCalendarBoxContentRecipient(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(15.dp).fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Rounded.People,
                contentDescription = null,
                Modifier
                    .border(1.dp, Color.White.copy(.3f), CircleShape)
                    .size(30.dp)
                    .background(Color.White.copy(.2f), CircleShape)
                    .padding(7.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "1st year",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

    }
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
                    text = "Zs's Advent",
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
    modifier: Modifier) {
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


@Composable
fun HomeHeader(currentUser: FirebaseUser?) {
    if (currentUser != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentUser.photoUrl)
                        .placeholder(R.drawable.gift)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .border(2.dp, Color.White, CircleShape)
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Box(modifier = Modifier
                    .offset(x = 5.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(3.dp)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null, tint = Color.Black)
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = "Hi " + currentUser.displayName.toString() + " 👋🏻 ", style = MaterialTheme.typography.bodySmall.copy(platformStyle = PlatformTextStyle(
                    emojiSupportMatch = EmojiSupportMatch.All
                )), color = Color.LightGray)

                Text(text = "Welcome Back!", fontWeight = FontWeight.Bold)
            }
        }
    }
}
