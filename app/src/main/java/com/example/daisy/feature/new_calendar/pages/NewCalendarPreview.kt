package com.example.daisy.feature.new_calendar.pages

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.common.elements.grayScale
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumPurple
import com.example.daisy.ui.theme.Purple
import io.getstream.sketchbook.Sketchbook
import io.getstream.sketchbook.rememberSketchbookController

@Composable
fun NewCalendarPreview() {
    NewCalendarPreviewContent()
}

@Composable
fun NewCalendarPreviewContent(
    viewModel: NewCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            NewCalendarPreviewCard(
                calendarUi = state,
                modifier = Modifier
                    .alpha(.1f)
                    .blur(3.dp)
                    .grayScale()
            )
            NewCalendarPreviewCard(
                calendarUi = state,
                modifier = Modifier,
                allowSketch = true
            )

            NewCalendarPreviewCard(
                calendarUi = state,
                modifier = Modifier
                    .alpha(.1f)
                    .blur(3.dp)
                    .grayScale()
            )
        }
        NewCalendarPreviewHeader()
    }
}

@Composable
fun NewCalendarPreviewCard(
    allowSketch: Boolean = false,
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    val sketchbookController = rememberSketchbookController()
    sketchbookController.setPaintColor(MediumPurple)

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            CalendarItemBackground(
                borderColor = Color.White.copy(0.3f),
                backgroundColor = Purple,
                modifier = Modifier.height(220.dp)
            )
            if(allowSketch) {
                Sketchbook(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(20.dp)),
                    controller = sketchbookController,
                    backgroundColor = Color.Transparent
                )
            }
            CalendarItemContent(
                calendarUi = calendarUi,
                modifier = Modifier.matchParentSize(),
                type = Type.RECEIVED
            )
        }

        if(allowSketch) {
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                LargeIcon(
                    imageVector = Icons.Default.Undo,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        sketchbookController.undo()
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                LargeIcon(
                    imageVector = Icons.Default.Redo,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        sketchbookController.redo()
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                LargeIcon(
                    imageVector = Icons.Default.Delete,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        sketchbookController.clear()
                    }
                )
            }
        }
    }

}

@Composable
private fun NewCalendarPreviewHeader() {
    Column(
        Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "I'm drawing...",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Now you can see what your calendar will look like for your recipient. Paint something on it!",
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}
