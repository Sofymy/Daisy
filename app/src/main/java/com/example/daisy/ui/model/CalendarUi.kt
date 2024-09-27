package com.example.daisy.ui.model

import kotlin.random.Random

data class CalendarUi(
    val id: String = Random.nextInt().toString(),
    val title: String = "",
    val icon: IconOptionUi = IconOptionUi.LOVE,
    val dateRange: DateRangeUi = DateRangeUi(),
    val recipients: List<String> = listOf(""),
    val sender: UserUi = UserUi(),
    val code: String? = null
)