package com.example.daisy.feature.calendars

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.daisy.R
import com.example.daisy.feature.new_calendar.pages.LargeIcon
import com.example.daisy.ui.common.elements.conditional
import com.example.daisy.ui.common.elements.pluralize
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.DarkPurple
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import java.time.Duration.between
import java.time.LocalDate

val stroke = Stroke(width = 10f,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
)

@Composable
fun CalendarItemBackground(
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier
) {
    Box(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .blur(20.dp)
            .drawBehind {
                drawCircle(Color.Black.copy(.2f), center = Offset(150f, 150f), radius = 450f)
            }
            .padding(20.dp)
    )
}


enum class Type {
    CREATED,
    RECEIVED
}

@Composable
fun CalendarItemContent(
    type: Type,
    calendarUi: CalendarUi,
    modifier: Modifier = Modifier,
    isEditableOnLongClick: Boolean = false,
    onClickEditRecipient: (() -> Unit)? = null,
    onClickEditTitleOrIcon: (() -> Unit)? = null,
    onClickEditDates: (() -> Unit)? = null,
    onClickToCard: (() -> Unit?)? = null,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotateLock = infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = ""
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        CalendarItemContentSenderOrRecipient(
            type = type,
            calendarUi = calendarUi,
            modifier = Modifier.align(Alignment.TopStart),
            isEditableOnLongClick = isEditableOnLongClick,
            onClickEditRecipient = onClickEditRecipient,
            onClickEditTitleOrIcon = onClickEditTitleOrIcon
        )
        CalendarItemContentDayCounter(
            calendarUi = calendarUi,
            modifier = Modifier.align(Alignment.TopEnd),
            isEditableOnLongClick = isEditableOnLongClick,
            onClickEditDates = onClickEditDates
        )
        when(type){
            Type.CREATED -> {
                CalendarItemRecipients(
                    calendarUi = calendarUi,
                    colors = listOf(Purple, DarkPurple, Blue),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    isEditableOnLongClick = isEditableOnLongClick
                )
            }
            Type.RECEIVED -> {
                CalendarItemContentLockButton(
                    rotateLock = rotateLock.value,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    isLocked = calendarUi.days.dateRange.dateStart.atStartOfDay() >= LocalDate.now().plusDays(1).atStartOfDay()
                )
            }
        }
        CalendarItemContentOpenText(
            calendarUi = calendarUi,
            modifier = Modifier.align(Alignment.BottomStart),
            isEditableOnLongClick = isEditableOnLongClick,
            onClickEditDates = onClickEditDates
        )
    }
}

@Composable
fun CalendarItemRecipients(
    calendarUi: CalendarUi,
    colors: List<Color>,
    modifier: Modifier,
    isEditableOnLongClick: Boolean = false
) {
    val numberOfCreatedCalendarRecipients = remember {
        mutableIntStateOf(calendarUi.recipients.size)
    }

    Box(modifier = modifier.padding(bottom = 20.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((-10).dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        ) {
            calendarUi.recipients.forEachIndexed { i, recipient ->
                if (i < 2 || numberOfCreatedCalendarRecipients.intValue == 3) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.White.copy(.1f), CircleShape)
                            .clip(CircleShape)
                            .background(colors[i])
                            .size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if(recipient.isBlank()) Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        else Text(recipient[0].toString())
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.White.copy(.1f), CircleShape)
                            .clip(CircleShape)
                            .background(colors[i])
                            .size(50.dp),
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
fun CalendarItemContentSenderOrRecipient(
    calendarUi: CalendarUi,
    modifier: Modifier,
    isEditableOnLongClick: Boolean = false,
    type: Type,
    onClickEditRecipient: (() -> Unit)?,
    onClickEditTitleOrIcon: (() -> Unit)?
) {
    Box(
        modifier
            .padding(top = 20.dp, start = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LargeIcon(
                imageVector = calendarUi.icon.icon,
                modifier = Modifier
                    .conditional(isEditableOnLongClick) {
                        Modifier
                            .clickable {
                                if (onClickEditRecipient != null) {
                                    onClickEditRecipient()
                                }
                            }
                            .drawBehind {
                                drawRoundRect(color = Purple, style = stroke)
                            }
                            .padding(5.dp)
                    }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = calendarUi.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.conditional(isEditableOnLongClick) {
                        Modifier
                            .clickable {
                                if (onClickEditTitleOrIcon != null) {
                                    onClickEditTitleOrIcon()
                                }
                            }
                            .drawBehind {
                                drawRoundRect(color = Purple, style = stroke)
                            }
                            .padding(5.dp)
                    }
                )
                (if(type == Type.RECEIVED) calendarUi.sender.name?.let {
                    stringResource(
                        R.string.from,
                        it
                    )
                } else stringResource(
                    R.string.to,
                    if (calendarUi.recipients.isEmpty()) "xy" else calendarUi.recipients[0]
                ))?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .conditional(isEditableOnLongClick) {
                                Modifier
                                    .clickable {
                                        if (onClickEditRecipient != null) {
                                            onClickEditRecipient()
                                        }
                                    }
                                    .drawBehind {
                                        drawRoundRect(color = Purple, style = stroke)
                                    }
                                    .padding(5.dp)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarItemContentDayCounter(
    calendarUi: CalendarUi,
    modifier: Modifier,
    isEditableOnLongClick: Boolean = false,
    onClickEditDates: (() -> Unit)?
) {
    val daysBetweenStartAndEnd = between(calendarUi.days.dateRange.dateStart.atStartOfDay(), calendarUi.days.dateRange.dateEnd.atStartOfDay()).toDays().toInt() + 1

    Box(
        modifier
            .padding(20.dp)
            .conditional(isEditableOnLongClick) {
                Modifier
                    .clickable {
                        if (onClickEditDates != null) {
                            onClickEditDates()
                        }
                    }
                    .drawBehind {
                        drawRoundRect(color = Purple, style = stroke)
                    }
                    .padding(5.dp)
            }
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
            Text(text = stringResource(R.string.days), color = Color.White)
        }
    }
}

@Composable
fun CalendarItemContentLockButton(
    rotateLock: Float,
    modifier: Modifier,
    isLocked: Boolean
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
            Text(text = if(isLocked) stringResource(R.string.closed) else stringResource(R.string.open), color = Color.White.copy(0.3f))
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = if(isLocked)Icons.Default.Lock else Icons.Default.LockOpen,
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
    modifier: Modifier,
    isEditableOnLongClick: Boolean = false,
    onClickEditDates: (() -> Unit)?
) {
    val daysBetweenStartAndToday = between(LocalDate.now().atStartOfDay(), calendarUi.days.dateRange.dateStart.atStartOfDay()).toDays().toInt()
    val daysBetweenEndAndToday = between(LocalDate.now().atStartOfDay(), calendarUi.days.dateRange.dateEnd.atStartOfDay()).toDays().toInt()


    val stroke = Stroke(width = 10f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier
            .padding(start = 20.dp, bottom = 30.dp)
            .conditional(isEditableOnLongClick) {
                Modifier
                    .clickable {
                        if (onClickEditDates != null) {
                            onClickEditDates()
                        }
                    }
                    .drawBehind {
                        drawRoundRect(color = Purple, style = stroke)
                    }
                    .padding(5.dp)
            },
        contentAlignment = Alignment.TopStart
    ) {
        Column() {

            if (calendarUi.days.dateRange.dateStart.isBefore(LocalDate.now().plusDays(1))) {

                if(daysBetweenEndAndToday < 0)
                    Text(
                        text = stringResource(R.string.ended_day, daysBetweenEndAndToday * (-1)).pluralize(daysBetweenEndAndToday*(-1)) + stringResource(
                            R.string.ago
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                else
                    Text(
                        text = stringResource(R.string.ends_in_day, daysBetweenEndAndToday).pluralize(daysBetweenEndAndToday),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
            } else {

                Text(
                    text = stringResource(R.string.opens_in_day, daysBetweenStartAndToday).pluralize(daysBetweenStartAndToday),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}