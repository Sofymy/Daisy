package com.example.daisy.domain.model

import android.graphics.Bitmap
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.IconOptionUi

data class Calendar(
    val id: String = "",
    val title: String = "",
    val icon: String = "",
    val dateRange: DateRange = DateRange(),
    val recipients: List<String> = emptyList(),
    val sender: User = User(),
    val code: String? = null
)

fun CalendarUi.toDomain(): Calendar {
    return Calendar(
        id = this.id,
        title = this.title,
        dateRange = this.dateRange.toDomain(),
        icon = this.icon.label,
        recipients = this.recipients,
        sender = this.sender.toDomain(),
        code = this.code
    )
}

fun Calendar.toUi(): CalendarUi {
    return CalendarUi(
        id = this.id,
        title = this.title,
        icon = IconOptionUi.fromLabel(this.icon) ?: IconOptionUi.LOVE,
        dateRange = this.dateRange.toUi(),
        recipients = this.recipients,
        sender = this.sender.toUi(),
        code = this.code
    )
}
