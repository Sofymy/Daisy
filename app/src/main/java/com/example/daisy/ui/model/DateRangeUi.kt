package com.example.daisy.ui.model

import com.example.daisy.feature.new_calendar.pages.calculateNumberOfDays
import java.time.LocalDate
import java.time.Period.between

data class DateRangeUi(
    val dateStart: LocalDate = LocalDate.now(),
    val dateEnd: LocalDate = LocalDate.now().plusMonths(1)
)
