package com.example.daisy.feature.calendars.created_calendars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.ui.common.state.ErrorContent
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumGrey



@Composable
fun CreatedCalendarEditorScreen(
    id: String?,
    viewModel: CreatedCalendarViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { id?.let { CreatedCalendarEditorUserEvent.GetCreatedCalendar(it) }
            ?.let { viewModel.onEvent(it) } },
    )

    CreatedCalendarEditorContent(state = state)
}

@Composable
fun CreatedCalendarEditorContent(
    state: CreatedCalendarEditorUiState
) {

    when {
        state.isError -> ErrorContent()
        state.isLoading -> LoadingContent()
        else -> CreatedCalendarEditorItems(calendarUi = state.calendar)
    }
}

@Composable
fun CreatedCalendarEditorItems(
    calendarUi: CalendarUi?
) {

    val editorSteps = createdCalendarEditorSteps()

    LazyColumn {
        items(editorSteps.chunked(2)){row ->
            Row {
                row.forEach {
                    CreatedCalendarEditorItem(
                        Modifier
                            .weight(1f)
                            .fillMaxSize(), it)
                }
                if (row.size < 2) {
                    Box(Modifier.weight(1f))
                }
            }
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