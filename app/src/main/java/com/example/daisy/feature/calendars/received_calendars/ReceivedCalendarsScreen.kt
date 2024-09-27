package com.example.daisy.feature.calendars.received_calendars

import android.view.ViewTreeObserver
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.LightPurple
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple

@Composable
fun ReceivedCalendarsScreen(
){
    ReceivedCalendarsContent()
}

@Composable
fun ReceivedCalendarsContent(
    viewModel: ReceivedCalendarsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents {
        viewModel.onEvent(ReceivedCalendarsUserEvent.GetReceivedCalendars)
    }

    Column(
        Modifier
            .fillMaxSize()) {
        when {
            state.isError -> ErrorContent()
            state.isLoading -> LoadingContent()
            else -> {
                if(state.calendars.isNotEmpty())
                    LazyColumn {
                        item {
                            Spacer(modifier = Modifier.height(35.dp))
                        }
                        items(
                            items = state.calendars.sortedBy { it.dateRange.dateStart },
                        ){calendar ->
                            ReceivedCalendarItem(
                                calendarUi = calendar,
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                else ReceivedCalendarsEmpty(
                    send = {
                        viewModel.onEvent(ReceivedCalendarsUserEvent.AddReceivedCalendarByCode(it))
                    },
                )
            }
        }
    }

}

@Composable
fun ReceivedCalendarItem(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        CalendarItemBackground(
            borderColor = Color.White.copy(0.3f),
            backgroundColor = Purple
        )
        CalendarItemContent(
            calendarUi = calendarUi,
            modifier = Modifier.matchParentSize(),
            type = Type.RECEIVED
            //onClickItem = {  }
        )
    }
}

@Composable
fun ReceivedCalendarsEmpty(
    send: (String) -> Unit,
) {
    val showUfo = remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val keyboardState = keyboardAsState()

    LaunchedEffect(Unit) {
        showUfo.value = true
    }
    
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                when (keyboardState.value) {
                    Keyboard.Opened -> {
                        focusManager.clearFocus()
                    }

                    Keyboard.Closed -> {
                        focusRequester.requestFocus()
                    }
                }
            }
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = showUfo.value,
            enter = slideInVertically(
                initialOffsetY = { fullWidth -> -fullWidth },
                animationSpec = tween(3000)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullWidth -> fullWidth },
                animationSpec = tween(1000)
            )
        ) {
            ReceivedCalendarsAnimatedUFO()
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "This space is empty.", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Add a calendar by code", color = Purple)
            Spacer(modifier = Modifier.height(20.dp))
            ReceivedCalendarsCodeInput(send, focusRequester)
        }
    }
}

@Composable
fun ReceivedCalendarsCodeInput(
    send: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val value = remember {
        mutableStateOf("")
    }

    LaunchedEffect(value.value.length) {
        if (value.value.length == 6){
            send(value.value)
        }
    }

    val keyboard by keyboardAsState()
    BasicTextField(
        modifier = Modifier
            .focusRequester(focusRequester)
        ,
        value = value.value,
        singleLine = true,
        onValueChange = { if (it.length <= 6) value.value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        decorationBox = { innerTextField ->
            Box {
                CompositionLocalProvider(
                    LocalTextSelectionColors.provides(
                        TextSelectionColors(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                ) {
                    Box(modifier = Modifier.drawWithContent { }) {
                        innerTextField()
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    repeat(6) { index ->
                        val currentChar = value.value.getOrNull(index)

                        Box {
                            Column(modifier = Modifier
                                .padding(7.dp)
                                .clip(RoundedCornerShape(20))
                                .background(MediumGrey)
                                .border(
                                    1.dp,
                                    if (keyboard == Keyboard.Opened && index <= value.value.length) {
                                        Purple
                                    } else {
                                        Color.White.copy(
                                            0.1f
                                        )
                                    },
                                    RoundedCornerShape(20)
                                )
                                .padding(10.dp)
                            ) {
                                Text(
                                    text = (currentChar ?: 0).toString(),
                                    color = if(currentChar == null) Color.White.copy(.03f) else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun ReceivedCalendarsAnimatedUFO() {
    val rotate = rememberInfiniteTransition(label = "")
    val infiniteRotation = rotate.animateFloat(initialValue = 0.5f, targetValue = 0.7f, animationSpec = infiniteRepeatable(
        tween(3000), repeatMode = RepeatMode.Reverse
    ), label = ""
    )

    Canvas(
        modifier = Modifier
            .offset(y = (-40).dp)
            .fillMaxSize(0.8f)
    ) {
        drawOval(
            color = LightPurple,
            topLeft = Offset(size.width * 0.35f, size.height * 0.1f),
            size = Size(size.width * 0.3f, size.height * 0.12f)
        )

        val path = Path().apply {
            val width = size.width
            val height = size.height
            val leftTop = width * 0.4f

            moveTo(leftTop, size.height * 0.20f)                      // Top-left corner
            lineTo(width - leftTop, size.height * 0.20f)              // Top-right corner
            lineTo(size.width*0.8f, height*infiniteRotation.value)                    // Bottom-right corner
            lineTo(size.width*0.2f, height*infiniteRotation.value)                       // Bottom-left corner
            close()
        }

        drawPath(
            path = path,
            brush = Brush.verticalGradient(listOf(Color.White, Color.Transparent, Color.Transparent)),
        )

        drawOval(
            color = Purple,
            topLeft = Offset(size.width * 0.25f, size.height * 0.15f),
            size = Size(size.width * 0.5f, size.height * 0.07f),
        )

        drawOval(
            color = Color.White.copy(.3f),
            topLeft = Offset(size.width * 0.25f, size.height * 0.15f),
            size = Size(size.width * 0.5f, size.height * 0.07f),
            style = Stroke(3f)
        )


        val lightOffsetY = size.height * 0.18f
        val lightRadius = 10f
        val lightPositions = listOf(
            Offset(size.width * 0.3f, lightOffsetY),
            Offset(size.width * 0.4f, lightOffsetY*0.94f),
            Offset(size.width * 0.5f, lightOffsetY*0.92f),
            Offset(size.width * 0.6f, lightOffsetY*0.94f),
            Offset(size.width * 0.7f, lightOffsetY)
        )
        for (position in lightPositions) {
            drawCircle(color = Color.LightGray.copy(0.3f), radius = lightRadius, center = position)
        }
    }
}

internal enum class Keyboard {
    Opened, Closed
}

@Composable
internal fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}