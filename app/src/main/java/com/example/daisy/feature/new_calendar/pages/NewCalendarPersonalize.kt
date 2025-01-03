package com.example.daisy.feature.new_calendar.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.feature.calendars.CalendarItemBackground
import com.example.daisy.feature.calendars.CalendarItemContent
import com.example.daisy.feature.calendars.Type
import com.example.daisy.feature.new_calendar.NewCalendarTypewriterText
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.IconOptionUi
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple

@Composable
fun NewCalendarPersonalize() {
    NewCalendarPersonalizeContent()
}

@Composable
fun NewCalendarPersonalizeContent(
    viewModel: NewCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewCalendarPersonalizeForm(
        state = state,
        onIconChange = {
            viewModel.onEvent(NewCalendarUserEvent.IconChanged(it))
        },
        onTitleChange = {
            viewModel.onEvent(NewCalendarUserEvent.TitleChanged(it))
        }
    )
}

@Composable
fun NewCalendarPersonalizeForm(
    state: CalendarUi,
    onTitleChange: (String) -> Unit,
    onIconChange: (IconOptionUi) -> Unit
) {
    val options = IconOptionUi.entries.toTypedArray()
    val selectedValue = remember { mutableStateOf(IconOptionUi.LOVE) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            NewCalendarPersonalizeHeader()
        }
        item {
            NewCalendarPersonalizeCard(
                calendarUi = state,
                modifier = Modifier.graphicsLayer {
                    scaleY = .7f
                    scaleX = .7f
                }
            )
        }
        item {
            NewCalendarPersonalizeTitle(
                state = state,
                onTitleChange = onTitleChange
            )
        }
        item {
            NewCalendarPersonalizeIconOptionsList(
                options = options,
                state = state,
                selectedValue = selectedValue.value,
                onOptionSelected = { option ->
                    selectedValue.value = option
                    onIconChange(option)
                },
            )
        }
    }
}

@Composable
fun NewCalendarPersonalizeIconOptionsList(
    state: CalendarUi,
    options: Array<IconOptionUi>,
    onOptionSelected: (IconOptionUi) -> Unit,
    selectedValue: IconOptionUi
) {
    Column(
        Modifier
            .padding(bottom = 50.dp, top = 30.dp)
            .fillMaxSize()
    ) {

        Text(text = stringResource(R.string.select_an_icon), fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
        ) {
            item { Spacer(modifier = Modifier.width(20.dp)) }
            items(options){option ->
                NewCalendarPersonalizeIconOptionItem(
                    option = option,
                    isSelected = option == selectedValue,
                    onOptionSelected = onOptionSelected,
                    state = state,
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun NewCalendarPersonalizeIconOptionItem(
    option: IconOptionUi,
    isSelected: Boolean,
    onOptionSelected: (IconOptionUi) -> Unit,
    state: CalendarUi,
) {
    Column(
        modifier = Modifier
            .border(
                1.dp, if (isSelected) Purple else Color.White.copy(.1f),
                RoundedCornerShape(20.dp)
            )
            .background(
                MediumGrey,
                RoundedCornerShape(20.dp)
            )
            .background(
                if (isSelected) Purple.copy(.05f) else MediumGrey,
                RoundedCornerShape(20.dp)
            )
            .selectable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                selected = isSelected,
                onClick = {
                    onOptionSelected(option)
                }
            )
    ) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LargeIcon(imageVector = option.icon)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = option.label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(.5f))
        }
    }
}

@Composable
fun NewCalendarPersonalizeTitle(
    state: CalendarUi,
    onTitleChange: (String) -> Unit
) {
    Column {
        PrimaryTextField(
            value = state.title,
            onValueChange = { newValue ->
                onTitleChange(newValue)
            },
            icon = Icons.Default.Title,
            placeholderText = stringResource(R.string.enter_a_title)
        )
    }
}

@Composable
fun NewCalendarPersonalizeCard(
    calendarUi: CalendarUi,
    modifier: Modifier,
) {

    Box(
        modifier
            .padding(15.dp)
    ) {
        CalendarItemBackground(
            borderColor = Color.White.copy(0.3f),
            backgroundColor = Purple,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
        )

        CalendarItemContent(
            type = Type.RECEIVED,
            calendarUi = calendarUi,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Composable
private fun NewCalendarPersonalizeHeader() {
    Column(
        Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewCalendarTypewriterText(
            baseText = stringResource(R.string.i_m),
            underlinedText = stringResource(R.string.personalizing)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.customize_your_calendar_add_a_title_and_icon),
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun LargeIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier
){
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier
            .border(1.dp, Color.White.copy(.3f), CircleShape)
            .size(50.dp)
            .background(Color.White.copy(.2f), CircleShape)
            .padding(13.dp)
    )
}