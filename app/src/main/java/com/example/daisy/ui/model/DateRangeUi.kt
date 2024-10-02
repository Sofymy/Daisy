package com.example.daisy.ui.model

import java.time.LocalDate
import java.time.Period.between

data class DateRangeUi(
    val dateStart: LocalDate = LocalDate.now(),
    val dateEnd: LocalDate = LocalDate.now().plusMonths(1)
){
    fun calculateDays(): List<DayUi> {
        val daysList = mutableListOf<DayUi>()
        var currentDate = dateStart

        while (!currentDate.isAfter(dateEnd)) {
            daysList.add(DayUi(date = currentDate))
            currentDate = currentDate.plusDays(1)
        }

        return daysList
    }
}
