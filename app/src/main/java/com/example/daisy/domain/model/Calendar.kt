package com.example.daisy.domain.model

import com.example.daisy.ui.model.CalendarUi

data class Calendar(
    val id: String = "",
    val dateRange: DateRange = DateRange(),
    val recipients: List<String> = emptyList(),
    val sender: User = User(),
    val code: String? = null
)

fun CalendarUi.toDomain(): Calendar {
    return Calendar(
        id = this.id,
        dateRange = this.dateRange.toDomain(),
        recipients = this.recipients,
        sender = this.sender.toDomain(),
        code = this.code
    )
}

fun Calendar.toUi(): CalendarUi {
    return CalendarUi(
        id = this.id,
        dateRange = this.dateRange.toUi(),
        recipients = this.recipients,
        sender = this.sender.toUi(),
        code = this.code
    )
}