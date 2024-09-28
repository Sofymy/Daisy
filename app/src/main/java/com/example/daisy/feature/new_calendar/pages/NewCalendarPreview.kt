package com.example.daisy.feature.new_calendar.pages

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.common.elements.grayScale
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.LightBlue
import com.example.daisy.ui.theme.MediumPurple
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.util.Constants
import io.getstream.sketchbook.PaintColorPalette
import io.getstream.sketchbook.Sketchbook
import io.getstream.sketchbook.SketchbookController
import io.getstream.sketchbook.rememberSketchbookController


@Composable
fun NewCalendarPreview() {
    NewCalendarPreviewContent()
}

@Composable
fun NewCalendarPreviewContent(viewModel: NewCalendarViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    Box {
        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
            repeat(3) {
                NewCalendarPreviewBackgroundCard(calendarUi = state)
            }
        }
        Column {
            NewCalendarPreviewHeader()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                NewCalendarPreviewForegroundCard(
                    calendarUi = state,
                    onDrawingChanged = {
                        viewModel.onEvent(NewCalendarUserEvent.DrawingChanged(it))
                    }
                )
            }
        }
    }
}

@Composable
fun NewCalendarPreviewBackgroundCard(calendarUi: CalendarUi) {
    NewCalendarPreviewCard(
        calendarUi = calendarUi,
        modifier = Modifier
            .alpha(0.1f)
            .blur(3.dp)
            .grayScale(),
        onDrawingChanged = {}
    )
}

@Composable
fun NewCalendarPreviewForegroundCard(
    calendarUi: CalendarUi,
    onDrawingChanged: (Bitmap) -> Unit
) {
    NewCalendarPreviewCard(
        calendarUi = calendarUi,
        modifier = Modifier,
        allowSketch = true,
        onDrawingChanged = onDrawingChanged
    )
}

@Composable
fun NewCalendarPreviewCard(
    allowSketch: Boolean = false,
    calendarUi: CalendarUi,
    modifier: Modifier,
    onDrawingChanged: (Bitmap) -> Unit
) {
    val sketchbookController = rememberSketchbookController()

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewCalendarPreviewCardBox(
            allowSketch = allowSketch,
            calendarUi = calendarUi,
            modifier = modifier,
            sketchbookController = sketchbookController,
            onDrawingChanged = onDrawingChanged
        )

        if(allowSketch)
            NewCalendarPreviewCardActions(sketchbookController = sketchbookController)
    }
}

@Composable
fun NewCalendarPreviewPaintPalette(
    sketchbookController: SketchbookController
) {
    PaintColorPalette(
        controller = sketchbookController,
        colorList = listOf(MediumPurple, LightBlue),
        onColorSelected = { _, color ->
            sketchbookController.setPaintColor(color)
        }
    )
}

@Composable
fun NewCalendarPreviewCardBox(
    allowSketch: Boolean,
    calendarUi: CalendarUi,
    modifier: Modifier,
    sketchbookController: SketchbookController,
    onDrawingChanged: (Bitmap) -> Unit
) {
    sketchbookController.setPaintColor(Color.White)

    Box(
        modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        CalendarItemBackground(
            borderColor = Color.White.copy(0.3f),
            backgroundColor = Purple,
            modifier = Modifier.height(220.dp)
        )
        if (allowSketch) {
            Sketchbook(
                modifier = Modifier
                    .alpha(Constants.CALENDAR_DRAWING_ALPHA)
                    .matchParentSize()
                    .clip(RoundedCornerShape(20.dp)),
                controller = sketchbookController,
                backgroundColor = Color.Transparent,
                onPathListener = {
                    onDrawingChanged(sketchbookController.getSketchbookBitmap().asAndroidBitmap())
                }
            )
        }
        CalendarItemContent(
            calendarUi = calendarUi,
            modifier = Modifier.matchParentSize(),
            type = Type.RECEIVED
        )
    }
}

@Composable
fun NewCalendarPreviewCardActions(
    sketchbookController: SketchbookController
) {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.End
    ) {
        NewCalendarPreviewActionIcon(
            imageVector = Icons.AutoMirrored.Filled.Undo,
            onClick = { sketchbookController.undo() }
        )
        Spacer(modifier = Modifier.width(15.dp))
        NewCalendarPreviewActionIcon(
            imageVector = Icons.AutoMirrored.Filled.Redo,
            onClick = { sketchbookController.redo() }
        )
        Spacer(modifier = Modifier.width(15.dp))
        NewCalendarPreviewActionIcon(
            imageVector = Icons.Default.Delete,
            onClick = { sketchbookController.clear() }
        )
    }
}

@Composable
fun NewCalendarPreviewActionIcon(imageVector: ImageVector, onClick: () -> Unit) {
    LargeIcon(
        imageVector = imageVector,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        )
    )
}

@Composable
fun NewCalendarPreviewHeader() {
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
    }
}
