package com.example.daisy.feature.create_calendar

import java.time.LocalDate

sealed class CreateCalendarUserEvent {
    data class StartChanged(val dateStart: LocalDate): CreateCalendarUserEvent()
    data class EndChanged(val dateEnd: LocalDate): CreateCalendarUserEvent()
    data class RecipientNameChanged(val recipientName: String): CreateCalendarUserEvent()
}