package com.example.daisy.domain.model

import com.example.daisy.ui.model.CalendarUi

data class Calendar(
    val id: String = "",
    val dateRange: DateRange = DateRange(),
    val recipient: User = User(),
    val sender: User = User()
)

fun CalendarUi.toDomain(): Calendar {
    return Calendar(
        id = this.id,
        dateRange = this.dateRange.toDomain(),
        recipient = this.recipient.toDomain(),
        sender = this.sender.toDomain()
    )
}

fun Calendar.toUi(): CalendarUi {
    return CalendarUi(
        id = this.id,
        dateRange = this.dateRange.toUi(),
        recipient = this.recipient.toUi(),
        sender = this.sender.toUi()
    )
}