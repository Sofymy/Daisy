package com.example.daisy.domain.usecases.calendar

import com.example.daisy.data.repository.calendar.CalendarRepository

class CalendarUseCases(
    val repository: CalendarRepository,
    val setCalendarUseCase: SetCalendarUseCase,
    val getCreatedCalendarsUseCase: GetCreatedCalendarsUseCase,
    val getReceivedCalendarsUseCase: GetReceivedCalendarsUseCase,
    val getReceivedCalendarDayUseCase: GetReceivedCalendarDayUseCase,
    val getCreatedCalendarUseCase: GetCreatedCalendarUseCase,
    val getCreatedCalendarDayUseCase: GetCreatedCalendarDayUseCase,
    val addReceivedCalendarByCodeUseCase: AddReceivedCalendarByCodeUseCase,
    val getCalendarDrawingUseCase: GetCalendarDrawingUseCase,
    val saveModificationsUseCase: SaveModificationsUseCase,
    val saveDayModificationsUseCase: SaveDayModificationsUseCase,
)