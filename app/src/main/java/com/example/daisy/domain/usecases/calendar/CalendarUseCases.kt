package com.example.daisy.domain.usecases.calendar

import com.example.daisy.data.repository.calendar.CalendarRepository

class CalendarUseCases(
    val repository: CalendarRepository,
    val setCalendarUseCase: SetCalendarUseCase,
    val getCreatedCalendarsUseCase: GetCreatedCalendarsUseCase
)