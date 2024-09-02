package com.example.daisy.feature.create_calendar

import com.example.daisy.ui.model.DateRangeUi
import com.example.daisy.ui.model.RecipientUi
import java.time.LocalDate

data class CreateCalendarUiState(
    val dateRange: DateRangeUi = DateRangeUi(),
    val recipient: RecipientUi = RecipientUi()
)