package com.example.daisy.feature.new_calendar.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        onFieldChange = { viewModel.onEvent(it) },
    )
}

@Composable
fun NewCalendarPersonalizeForm(
    state: CalendarUi,
    onFieldChange: (NewCalendarUserEvent) -> Unit,
) {
    val options = IconOptionUi.entries.toTypedArray()
    val selectedValue = remember { mutableStateOf(IconOptionUi.LOVE) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            NewCalendarPersonalizeHeader()
        }
        item {
            NewCalendarPersonalizeTitle(
                state = state,
                onFieldChange = onFieldChange
            )
        }
        item {
            NewCalendarPersonalizeIconOptionsList(
                options = options,
                state = state,
                selectedValue = selectedValue.value,
                onOptionSelected = { option ->
                    selectedValue.value = option
                    onFieldChange(NewCalendarUserEvent.IconChanged(option))
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
            .fillMaxSize()
            .padding(20.dp)
    ) {
        LazyRow(
        ) {
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
            Modifier.padding(20.dp).fillMaxWidth(),
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
    onFieldChange: (NewCalendarUserEvent) -> Unit
) {
    Column() {
        PrimaryTextField(
            value = state.title,
            onValueChange = { newValue ->
                onFieldChange(NewCalendarUserEvent.TitleChanged(newValue))
            },
            icon = Icons.Default.Title,
            placeholderText = "Enter a title"
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
        Text(
            text = "I'm personalizing...",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Select fancy title and icon for your calendar.",
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun LargeIcon(
    imageVector: ImageVector
){
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        Modifier
            .border(1.dp, Color.White.copy(.3f), CircleShape)
            .size(50.dp)
            .background(Color.White.copy(.2f), CircleShape)
            .padding(13.dp)
    )
}