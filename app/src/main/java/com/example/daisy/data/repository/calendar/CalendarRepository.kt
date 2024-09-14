package com.example.daisy.data.repository.calendar

import com.example.daisy.domain.model.Calendar
import kotlinx.coroutines.flow.Flow

interface CalendarRepository{
    fun setCalendar(calendar: Calendar)
    fun getCreatedCalendars(): Flow<List<Calendar?>>
    fun getReceivedCalendars(): Flow<List<Calendar?>>
    fun getCreatedCalendar(id: String): Flow<Calendar?>
    suspend fun addReceivedCalendarByCode(code: String)
}