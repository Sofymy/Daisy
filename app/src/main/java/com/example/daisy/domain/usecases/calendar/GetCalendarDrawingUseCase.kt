package com.example.daisy.domain.usecases.calendar

import android.graphics.Bitmap
import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.model.Calendar
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCalendarDrawingUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(fileName: String): Result<Bitmap?> {
        return try {
            Result.success(calendarRepository.getCalendarDrawing(fileName))
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}