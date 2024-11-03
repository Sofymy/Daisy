package com.example.daisy.domain.model

import com.example.daisy.ui.model.DayUi
import java.time.LocalDate

data class Day(
    val number: Int = 1,
    val date: String = LocalDate.now().toString(),
    val message: String = "",
    val image: String = ""
)

fun DayUi.toDomain(): Day {
    return Day(
        number = this.number,
        date = this.date.toString(),
        message = this.message,
        image = this.image
    )
}

fun Day.toUi(): DayUi {
    return DayUi(
        number = number,
        date = LocalDate.parse(this.date),
        message = this.message,
        image = this.image
    )
}