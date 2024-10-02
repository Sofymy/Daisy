package com.example.daisy.ui.model

import java.time.LocalDate

data class DayUi(
    val date: LocalDate = LocalDate.now(),
    val message: String = ""
)