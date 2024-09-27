@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.daisy.feature.new_calendar.pages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.common.elements.pluralize
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.gradient
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@Composable
fun NewCalendarDate() {
    NewCalendarDateContent()
}

@Composable
fun NewCalendarDateContent(
    viewModel: NewCalendarViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NewCalendarDateForm(state = state, onFieldChange = viewModel::onEvent)
}

@Composable
fun NewCalendarDateForm(
    state: CalendarUi,
    onFieldChange: (NewCalendarUserEvent) -> Unit,
) {
    val dateRangeState = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = FutureSelectableDates
    )
    val isCalendarValueSet by remember(dateRangeState.selectedStartDateMillis, dateRangeState.selectedEndDateMillis) {
        mutableStateOf(dateRangeState.selectedStartDateMillis != null && dateRangeState.selectedEndDateMillis != null)
    }

    UpdateCalendarDateRange(dateRangeState, onFieldChange)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewCalendarDateAnimatedContent(isCalendarValueSet, state)
        NewCalendarDateDateRangePicker(state = dateRangeState)
    }
}

@Composable
fun NewCalendarDateAnimatedContent(
    isCalendarValueSet: Boolean,
    state: CalendarUi
) {
    val numberOfDays = calculateNumberOfDays(state.dateRange.dateStart, state.dateRange.dateEnd)

    Text(
        text = "I'm creating...",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 20.dp)
    )

    AnimatedContent(
        targetState = isCalendarValueSet,
        transitionSpec = {
            fadeIn(tween(500)) togetherWith fadeOut(tween(500))
        },
        label = ""
    ) {
        if (it) {
            NewCalendarDateSelectedDays(numberOfDays)
        } else {
            NewCalendarDatePromptSelection()
        }
    }
}

@Composable
fun NewCalendarDateSelectedDays(
    numberOfDays: Long
) {
    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Calendar for ", color = Color.White.copy(.5f))
        Text(
            text = "$numberOfDays",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .border(1.dp, Color.White.copy(.1f), RoundedCornerShape(4.dp))
                .padding(vertical = 4.dp, horizontal = 8.dp),
        )
        Text(text = " day".pluralize(numberOfDays.toInt())+".", color = Color.White.copy(.5f))
    }
}

@Composable
fun NewCalendarDatePromptSelection() {
    Row(modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Select a date range to assign calendar", color = Color.White.copy(.5f))
    }
}

@Composable
fun UpdateCalendarDateRange(
    dateRangeState: DateRangePickerState,
    onFieldChange: (NewCalendarUserEvent) -> Unit,
) {
    LaunchedEffect(dateRangeState.selectedEndDateMillis, dateRangeState.selectedStartDateMillis) {
        onFieldChange(NewCalendarUserEvent.StartChanged(convertMillisToLocalDate(dateRangeState.selectedStartDateMillis ?: 0)))
        onFieldChange(NewCalendarUserEvent.EndChanged(convertMillisToLocalDate(dateRangeState.selectedEndDateMillis ?: 0)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalendarDateDateRangePicker(
    state: DateRangePickerState
) {
    Column(
        Modifier
            .padding(20.dp)
            .background(MediumGrey, RoundedCornerShape(30.dp))
            .border(1.dp, Color.White.copy(.1f), RoundedCornerShape(30.dp))
    ) {
        DateRangePicker(
            state = state,
            showModeToggle = false,
            title = {},
            headline = {
                NewCalendarDateRangeDisplay(state)
            }
        )
    }
}

@Composable
fun NewCalendarDateRangeDisplay(state: DateRangePickerState) {
    Row(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Box(Modifier.weight(1f)) {
            Text(text = state.selectedStartDateMillis?.let { getFormattedDate(it) } ?: "Start Date")
        }
        Box(Modifier.weight(1f)) {
            Text(text = state.selectedEndDateMillis?.let { getFormattedDate(it) } ?: "End Date")
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getFormattedDate(timeInMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return java.text.SimpleDateFormat("dd/MM/yyyy").format(calendar.timeInMillis)
}

fun calculateNumberOfDays(startDate: LocalDate, endDate: LocalDate): Long {
    return Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1
}

fun convertMillisToLocalDate(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    val instant = Instant.ofEpochMilli(millis)
    return instant.atZone(zoneId).toLocalDate()
}

@OptIn(ExperimentalMaterial3Api::class)
object FutureSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}
