package com.example.daisy.domain.usecases.calendar

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Calendar
import javax.inject.Inject

class SetCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(calendar: Calendar) =
        calendarRepository.setCalendar(calendar)
}