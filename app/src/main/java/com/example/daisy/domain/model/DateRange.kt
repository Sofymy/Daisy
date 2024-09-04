package com.example.daisy.domain.model

import com.example.daisy.ui.model.DateRangeUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DateRange(
    val dateStart: String = "",
    val dateEnd: String = "",
)

fun DateRangeUi.toDomain(): DateRange {
    return DateRange(
        dateStart = this.dateStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        dateEnd = this.dateEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
}

fun DateRange.toUi(): DateRangeUi {
    return DateRangeUi(
        dateStart = LocalDate.parse(this.dateStart),
        dateEnd = LocalDate.parse(this.dateEnd)
    )
}