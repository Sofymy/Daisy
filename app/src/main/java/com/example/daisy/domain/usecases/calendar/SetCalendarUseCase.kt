package com.example.daisy.domain.usecases.calendar

import android.graphics.Bitmap
import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Calendar
import javax.inject.Inject

class SetCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    operator fun invoke(calendar: Calendar, drawing: Bitmap?) =
        calendarRepository.setCalendar(calendar, drawing)
}