package com.example.daisy.domain.model

import com.example.daisy.ui.model.DayUi
import java.time.LocalDate

data class Day(
    val date: String = LocalDate.now().toString(),
    val message: String = ""
)

fun DayUi.toDomain(): Day {
    return Day(
        date = this.date.toString(),
        message = this.message
    )
}

fun Day.toUi(): DayUi {
    return DayUi(
        date = LocalDate.parse(this.date),
        message = this.message
    )
}