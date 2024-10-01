package com.example.daisy.domain.usecases.calendar

import android.graphics.Bitmap
import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Calendar
import javax.inject.Inject

class SaveModificationsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    operator fun invoke(calendar: Calendar) =
        calendarRepository.saveModifications(calendar)
}