package com.example.daisy.domain.di

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.domain.usecases.calendar.GetCreatedCalendarsUseCase
import com.example.daisy.domain.usecases.calendar.SetCalendarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarUseCasesModule {

    @Provides
    @Singleton
    fun provideCalendarUseCases(
        calendarRepository: CalendarRepository,
        setCalendarUseCase: SetCalendarUseCase,
        getCreatedCalendarsUseCase: GetCreatedCalendarsUseCase
    ): CalendarUseCases {
        return CalendarUseCases(
            repository = calendarRepository,
            setCalendarUseCase = setCalendarUseCase,
            getCreatedCalendarsUseCase = getCreatedCalendarsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideSetCalendarUseCase(
        calendarRepository: CalendarRepository
    ): SetCalendarUseCase {
        return SetCalendarUseCase(calendarRepository)
    }

    @Provides
    @Singleton
    fun provideGetCreatedCalendarUseCase(
        calendarRepository: CalendarRepository
    ): GetCreatedCalendarsUseCase {
        return GetCreatedCalendarsUseCase(calendarRepository)
    }

}