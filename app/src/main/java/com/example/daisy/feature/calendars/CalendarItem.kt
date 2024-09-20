package com.example.daisy.feature.calendars

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.daisy.ui.common.elements.pluralize
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.MediumGrey
import java.time.Duration.between
import java.time.LocalDate

@Composable
fun CalendarItemBackground(
    borderColor: Color,
    backgroundColor: Color
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .height(220.dp)
            .blur(20.dp)
            .drawBehind {
                drawCircle(Color.Black.copy(.2f), center = Offset(150f, 150f), radius = 450f)
            }
            .padding(20.dp)
    )
}

@Composable
fun CalendarItemContent(
    calendarUi: CalendarUi,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotateLock = infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = ""
    )

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        CalendarItemContentSender(calendarUi, Modifier.align(Alignment.TopStart))
        CalendarItemContentDayCounter(calendarUi, Modifier.align(Alignment.TopEnd))
        CalendarItemContentLockButton(rotateLock.value, Modifier.align(Alignment.BottomEnd))
        CalendarItemContentOpenText(calendarUi, Modifier.align(Alignment.BottomStart))
    }
}

@Composable
fun CalendarItemContentSender(
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
fun CalendarItemContentDayCounter(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    val daysBetweenStartAndEnd = between(calendarUi.dateRange.dateStart.atStartOfDay(), calendarUi.dateRange.dateEnd.atStartOfDay()).toDays().toInt()

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
                text = daysBetweenStartAndEnd.toString(),
                color = Color.White.copy(.5f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = "days", color = Color.White)
        }
    }
}

@Composable
fun CalendarItemContentLockButton(
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
fun CalendarItemContentOpenText(
    calendarUi: CalendarUi,
    modifier: Modifier
) {
    val daysBetween = between(LocalDate.now().atStartOfDay(), calendarUi.dateRange.dateStart.atStartOfDay()).toDays().toInt()

    Box(
        modifier
            .padding(start = 20.dp, bottom = 30.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column() {
            Text(
                text = "Opens in $daysBetween day".pluralize(daysBetween),
                fontWeight = FontWeight.Bold
            )
        }
    }
}