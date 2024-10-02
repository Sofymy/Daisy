package com.example.daisy.ui.model

data class DaysUi(
    val dateRange: DateRangeUi = DateRangeUi(),
    val days: List<DayUi> = dateRange.calculateDays()
)