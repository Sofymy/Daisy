package com.example.daisy.domain.usecases.calendar

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Calendar
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCreatedCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(id: String): Result<Calendar?> {
        return try {
            Result.success(calendarRepository.getCreatedCalendar(id).first())
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}