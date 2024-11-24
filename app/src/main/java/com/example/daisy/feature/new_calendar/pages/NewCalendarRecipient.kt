package com.example.daisy.feature.new_calendar.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.feature.new_calendar.NewCalendarTypewriterText
import com.example.daisy.feature.new_calendar.NewCalendarUserEvent
import com.example.daisy.feature.new_calendar.NewCalendarViewModel
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.RecipientOption
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple

@Composable
fun NewCalendarRecipient() {
    NewCalendarRecipientContent()
}

@Composable
fun NewCalendarRecipientContent(
    viewModel: NewCalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewCalendarRecipientForm(
        state = state,
        onRecipientChange = {
            viewModel.onEvent(NewCalendarUserEvent.RecipientEmailChanged(it)) },
        onCodeOptionSelected = {
            viewModel.onEvent(NewCalendarUserEvent.CodeOptionSelected)
        },
        onRecipientOptionSelected = {
            viewModel.onEvent(NewCalendarUserEvent.RecipientOptionSelected)
        }
    )
}

@Composable
fun NewCalendarRecipientForm(
    state: CalendarUi,
    onRecipientChange: (String) -> Unit,
    onRecipientOptionSelected: () -> Unit,
    onCodeOptionSelected: () -> Unit
) {
    val options = RecipientOption.entries.toTypedArray()
    val selectedValue = remember {
        if(state.recipients.isNotEmpty()){
            mutableStateOf(RecipientOption.EMAIL)
        }
        else
            mutableStateOf(RecipientOption.CODE)
    }

    LazyColumn(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            NewCalendarRecipientHeader()
        }
        item {
            NewCalendarRecipientOptionsList(
                options = options,
                selectedValue = selectedValue.value,
                onOptionSelected = { option ->
                    selectedValue.value = option
                    handleOptionChange(option, onRecipientOptionSelected, onCodeOptionSelected)
                },
                state = state,
                onRecipientChange = onRecipientChange
            )
        }
    }
}

@Composable
private fun NewCalendarRecipientHeader() {
    NewCalendarTypewriterText(
        baseText = stringResource(R.string.i_m),
        underlinedText = stringResource(R.string.sharing)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = stringResource(R.string.select_a_delivery_option_to_guide_you_through_the_process),
        textAlign = TextAlign.Center,
        color = Color.White.copy(alpha = 0.5f)
    )
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun NewCalendarRecipientOptionsList(
    options: Array<RecipientOption>,
    selectedValue: RecipientOption,
    onOptionSelected: (RecipientOption) -> Unit,
    state: CalendarUi,
    onRecipientChange: (String) -> Unit
) {
    options.forEach { option ->
        NewCalendarRecipientOptionItem(
            option = option,
            isSelected = option == selectedValue,
            onOptionSelected = onOptionSelected,
            state = state,
            onRecipientChange = onRecipientChange
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun NewCalendarRecipientOptionItem(
    option: RecipientOption,
    isSelected: Boolean,
    onOptionSelected: (RecipientOption) -> Unit,
    state: CalendarUi,
    onRecipientChange: (String) -> Unit
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
                onClick = { onOptionSelected(option) }
            )
    ) {
        NewCalendarRecipientOptionContent(option, isSelected)
        AnimatedVisibility (isSelected && option == RecipientOption.EMAIL) {
            NewCalendarRecipientEmailField(state, onRecipientChange)
        }
    }
}

@Composable
fun NewCalendarRecipientOptionContent(
    option: RecipientOption,
    isSelected: Boolean
) {
    val label = stringResource(option.labelRes)
    val description = stringResource(option.descriptionRes)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Icon(imageVector = option.icon, contentDescription = null)
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(.5f)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            modifier = Modifier.size(35.dp),
            tint = if (isSelected) Purple else Color.White.copy(.1f),
            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null
        )
    }
}

@Composable
fun NewCalendarRecipientEmailField(
    state: CalendarUi,
    onRecipientChange: (String) -> Unit
) {
    Column {
        PrimaryTextField(
            value = if(state.recipients.isNotEmpty()) state.recipients.first() else "",
            onValueChange = { newValue ->
                onRecipientChange(newValue)
            },
            icon = Icons.Default.AlternateEmail,
            placeholderText = stringResource(R.string.your_recipient_s_email)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun handleOptionChange(
    option: RecipientOption,
    onRecipientOptionSelected: () -> Unit,
    onCodeOptionSelected: () -> Unit
) {
    when (option) {
        RecipientOption.EMAIL -> onRecipientOptionSelected()
        RecipientOption.CODE -> onCodeOptionSelected()
    }
}
