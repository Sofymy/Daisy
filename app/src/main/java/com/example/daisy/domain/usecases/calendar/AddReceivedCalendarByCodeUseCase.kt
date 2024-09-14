package com.example.daisy.domain.usecases.calendar

import android.util.Log
import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Calendar
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddReceivedCalendarByCodeUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(code: String): Result<Unit> {
        return try {
            Result.success(calendarRepository.addReceivedCalendarByCode(code))
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}