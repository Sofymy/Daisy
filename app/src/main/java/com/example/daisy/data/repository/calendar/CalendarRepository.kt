package com.example.daisy.data.repository.calendar

import android.graphics.Bitmap
import com.example.daisy.domain.model.Calendar
import com.example.daisy.domain.model.Day
import kotlinx.coroutines.flow.Flow

interface CalendarRepository{
    fun setCalendar(calendar: Calendar, drawing: Bitmap?)
    fun getCreatedCalendars(): Flow<List<Calendar?>>
    fun getReceivedCalendars(): Flow<List<Calendar?>>
    fun getCreatedCalendar(id: String): Flow<Calendar?>
    fun getCreatedCalendarDay(id: String, number: Int): Flow<Calendar?>
    suspend fun addReceivedCalendarByCode(code: String)
    suspend fun getCalendarDrawing(filename: String): Bitmap?
    fun saveModifications(calendar: Calendar)
    fun saveDayModifications(id: String?, day: Day)
    fun getReceivedCalendarDay(id: String, number: Int):  Flow<Calendar?>
}