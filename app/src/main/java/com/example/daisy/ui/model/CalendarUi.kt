package com.example.daisy.ui.model

import com.example.daisy.domain.util.RecipientOption

data class CalendarUi(
    val dateRange: DateRangeUi = DateRangeUi(),
    val recipient: UserUi = UserUi(),
    val sender: UserUi = UserUi()
)