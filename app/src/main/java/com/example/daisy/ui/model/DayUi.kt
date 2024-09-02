package com.example.daisy.ui.model

import java.time.LocalDate

data class DayUi(
    val date: LocalDate,
    val message: String = ""
)