package com.example.daisy.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.daisy.ui.common.elements.pluralize
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.DarkBlue
import com.example.daisy.ui.theme.DarkPurple
import com.example.daisy.ui.theme.LightBlue
import com.example.daisy.ui.theme.MediumGrey
import java.time.Duration.between
import java.time.LocalDate


@Composable
fun HomeCreatedCalendars(
    createdCalendars: List<CalendarUi>,
    navigateToCreatedCalendars: () -> Unit
) {
    val numberOfReceivedCalendars = remember { mutableIntStateOf(createdCalendars.size) }
    val activeCalendar = remember { mutableIntStateOf(0) }
    val state = rememberLazyListState()
    val colors = listOf(DarkBlue, DarkPurple, LightBlue, DarkBlue)

    LaunchedEffect(createdCalendars) {
        numberOfReceivedCalendars.intValue = createdCalendars.size
        activeCalendar.intValue = 0
    }

    Spacer(modifier = Modifier.height(20.dp))
    HomeCreatedCalendarContentHeader(
        navigateToCreatedCalendars = navigateToCreatedCalendars
    )
    Spacer(modifier = Modifier.height(10.dp))
    HomeCreatedCalendarContentLazyRow(state, createdCalendars.sortedBy { it.days.dateRange.dateStart }, colors)
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun HomeCreatedCalendarContentHeader(
    navigateToCreatedCalendars: () -> Unit
) {
    Row(
        Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "My calendars", fontWeight = FontWeight.Bold)
        IconButton(onClick = {
            navigateToCreatedCalendars()
        }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos, null,
                modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun HomeCreatedCalendarContentLazyRow(
    state: LazyListState,
    createdCalendars: List<CalendarUi>,
    colors: List<Color>
) {
    LazyRow(
        state = state,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item { Spacer(modifier = Modifier.width(20.dp)) }
        itemsIndexed(
            items = createdCalendars,
            key = {_, item ->  item.id}
        ) { index, calendarUi ->
            HomeCreatedCalendarContentItem(index, state, calendarUi, colors)
        }
    }
}

@Composable
fun HomeCreatedCalendarContentItem(
    index: Int,
    state: LazyListState,
    calendarUi: CalendarUi,
    colors: List<Color>
) {
    val firstIndex = remember { derivedStateOf { state.firstVisibleItemIndex == index } }

    val size by animateFloatAsState(
        targetValue = if (firstIndex.value) 1.9f else 1.5f,
        animationSpec = spring(),
        label = ""
    )

    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Color.White.copy(.1f), RoundedCornerShape(10))
                .clip(RoundedCornerShape(10))
                .background(MediumGrey)
                .size(size = (size * 100).dp),
        ) {
            HomeCreatedCalendarContentTitleAndInfos(calendarUi, Modifier, firstIndex.value)
        }
        HomeCreatedCalendarContentRecipients(calendarUi, colors)
    }

    Spacer(modifier = Modifier.width(15.dp))
}


@Composable
fun HomeCreatedCalendarContentRecipients(
    calendarUi: CalendarUi,
    colors: List<Color>
) {
    val numberOfCreatedCalendarRecipients = remember {
        mutableIntStateOf(calendarUi.recipients.size)
    }

    Box(modifier = Modifier.offset(y = 10.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((-10).dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            calendarUi.recipients.forEachIndexed { i, recipient ->
                if (i < 2 || numberOfCreatedCalendarRecipients.intValue == 3) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.White.copy(.1f), CircleShape)
                            .clip(CircleShape)
                            .background(colors[i])
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if(recipient.isBlank()) "?" else recipient[0].toString())
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.White.copy(.1f), CircleShape)
                            .clip(CircleShape)
                            .background(colors[i])
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+${numberOfCreatedCalendarRecipients.intValue - i}")
                    }
                    return@Box
                }
            }

        }
    }
}


@Composable
fun HomeCreatedCalendarContentTitleAndInfos(
    calendarUi: CalendarUi,
    modifier: Modifier,
    firstVisibleIndex: Boolean
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        HomeCreatedCalendarContentTitleRow(calendarUi)
        HomeCreatedCalendarContentDateRow(calendarUi)
        HomeCreatedCalendarContentAnimatedVisibility(firstVisibleIndex, calendarUi)
    }
}

@Composable
fun HomeCreatedCalendarContentTitleRow(
    calendarUi: CalendarUi
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = calendarUi.icon.icon,
            contentDescription = null,
            Modifier
                .border(1.dp, Color.White.copy(.3f), CircleShape)
                .size(30.dp)
                .background(Color.White.copy(.2f), CircleShape)
                .padding(7.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = calendarUi.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun HomeCreatedCalendarContentDateRow(calendarUi: CalendarUi) {
    Row(
        verticalAlignment = Alignment.Bottom,
    ) {
        HomeCreatedCalendarContentDottedLine(
            startDate = calendarUi.days.dateRange.dateStart,
            endDate = calendarUi.days.dateRange.dateEnd
        )
        Column(
            modifier = Modifier
                .padding(end = 15.dp, start = 5.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = calendarUi.days.dateRange.dateStart.toString().replace("-", ".") + ".",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = calendarUi.days.dateRange.dateEnd.toString().replace("-", ".") + ".",
                color = Color.White.copy(.5f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun HomeCreatedCalendarContentAnimatedVisibility(
    firstVisibleIndex: Boolean,
    calendarUi: CalendarUi
) {
    AnimatedVisibility(
        visible = firstVisibleIndex,
        enter = fadeIn(tween(1000)),
        exit = fadeOut(tween(200))
    ) {
        Column(
            Modifier
                .padding(top = 10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(color = Color.White.copy(.1f))
            Spacer(modifier = Modifier.height(10.dp))
            HomeCreatedCalendarContentDateCountdown(calendarUi)
        }
    }
}

@Composable
fun HomeCreatedCalendarContentDateCountdown(calendarUi: CalendarUi) {
    val now = LocalDate.now()
    if (calendarUi.days.dateRange.dateStart.isBefore(now.plusDays(1))) {

        val daysBetween = between(now.atStartOfDay(), calendarUi.days.dateRange.dateEnd.atStartOfDay()).toDays().toInt()

        Text(
            text = "Ends in $daysBetween day".pluralize(daysBetween),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(.5f),
            fontWeight = FontWeight.Bold
        )
    } else {

        val daysBetween = between(now.atStartOfDay(), calendarUi.days.dateRange.dateStart.atStartOfDay()).toDays().toInt()

        Text(
            text = "Opens in $daysBetween day".pluralize(daysBetween),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(.5f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HomeCreatedCalendarContentDottedLine(
    startDate: LocalDate,
    endDate: LocalDate
) {
    Column(
        modifier = Modifier.padding(start = 15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, Color.White.copy(.4f), CircleShape)
                .size(10.dp)
                .background(
                    if (startDate.isBefore(
                            LocalDate
                                .now()
                                .plusDays(1)
                        )
                    ) Color.White.copy(.4f) else Color.Transparent, shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(20.dp)
        ) {
            drawLine(
                color = Color.Gray,
                start = center.copy(y = 0f),
                end = center.copy(y = size.height),
                strokeWidth = 5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .border(1.dp, Color.White.copy(.4f), CircleShape)
                .size(10.dp)
                .background(
                    if (endDate.isBefore(LocalDate.now())) Color.White.copy(.3f) else Color.Transparent,
                    shape = CircleShape
                )
        )
    }
}
