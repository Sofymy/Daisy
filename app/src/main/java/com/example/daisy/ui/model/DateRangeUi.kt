package com.example.daisy.ui.model

import java.time.LocalDate

data class DateRangeUi(
    val dateStart: LocalDate = LocalDate.now(),
    val dateEnd: LocalDate = LocalDate.now().plusMonths(1)
)
