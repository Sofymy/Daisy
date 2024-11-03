package com.example.daisy.ui.model

import com.example.daisy.feature.new_calendar.pages.calculateNumberOfDays

data class DaysUi(
    val dateRange: DateRangeUi = DateRangeUi(),
    var days: List<DayUi> = emptyList()
){
    init {
        if(days.isEmpty()) days = calculateDays()
    }
    fun calculateDays(): List<DayUi> {
        val daysList = mutableListOf<DayUi>()
        var currentDate = dateRange.dateStart

        while (!currentDate.isAfter(dateRange.dateEnd)) {
            daysList.add(DayUi(number = calculateNumberOfDays(dateRange.dateStart, currentDate).toInt(), date = currentDate))
            currentDate = currentDate.plusDays(1)
        }

        return daysList
    }
}