package com.example.daisy.data.repository.calendar

import android.graphics.Bitmap
import com.example.daisy.domain.model.Calendar
import kotlinx.coroutines.flow.Flow

interface CalendarRepository{
    fun setCalendar(calendar: Calendar, drawing: Bitmap?)
    fun getCreatedCalendars(): Flow<List<Calendar?>>
    fun getReceivedCalendars(): Flow<List<Calendar?>>
    fun getCreatedCalendar(id: String): Flow<Calendar?>
    suspend fun addReceivedCalendarByCode(code: String)
    suspend fun getCalendarDrawing(filename: String): Bitmap?
}