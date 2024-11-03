package com.example.daisy.domain.usecases.calendar

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Day
import javax.inject.Inject

class SaveDayModificationsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    operator fun invoke(day: Day, id: String?) =
        calendarRepository.saveDayModifications(
            day = day,
            id = id
        )
}