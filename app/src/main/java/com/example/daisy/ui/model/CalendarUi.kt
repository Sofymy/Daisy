package com.example.daisy.ui.model

import kotlin.random.Random

data class CalendarUi(
    val id: String = Random.nextInt().toString(),
    val dateRange: DateRangeUi = DateRangeUi(),
    val recipient: UserUi = UserUi(),
    val sender: UserUi = UserUi()
)