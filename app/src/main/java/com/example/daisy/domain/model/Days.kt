package com.example.daisy.domain.model

import com.example.daisy.ui.model.DaysUi

data class Days(
    val dateRange: DateRange = DateRange(),
    val days: List<Day> = emptyList()
)

fun DaysUi.toDomain(): Days {
    return Days(
        dateRange = dateRange.toDomain(),
        days = this.days.map { it.toDomain() }
    )
}

fun Days.toUi(): DaysUi {
    return DaysUi(
        dateRange = dateRange.toUi(),
        days = this.days.map { it.toUi() }
    )
}