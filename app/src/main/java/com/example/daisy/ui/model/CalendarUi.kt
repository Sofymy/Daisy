package com.example.daisy.ui.model

import kotlin.random.Random

data class CalendarUi(
    val id: String = Random.nextInt().toString(),
    val dateRange: DateRangeUi = DateRangeUi(),
    val recipients: List<String> = listOf(""),
    val sender: UserUi = UserUi(),
    val code: String? = null
)