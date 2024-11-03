package com.example.daisy.ui.model

import java.time.LocalDate

data class DayUi(
    val number: Int = 1,
    val date: LocalDate = LocalDate.now(),
    val message: String = "",
    val image: String = ""
)