package com.example.daisy.domain.di

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.usecases.calendar.AddReceivedCalendarByCodeUseCase
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.domain.usecases.calendar.GetCreatedCalendarUseCase
import com.example.daisy.domain.usecases.calendar.GetCreatedCalendarsUseCase
import com.example.daisy.domain.usecases.calendar.GetReceivedCalendarsUseCase
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
        getCreatedCalendarsUseCase: GetCreatedCalendarsUseCase,
        getCreatedCalendarUseCase: GetCreatedCalendarUseCase,
        getReceivedCalendarsUseCase: GetReceivedCalendarsUseCase,
        addReceivedCalendarByCodeUseCase: AddReceivedCalendarByCodeUseCase
    ): CalendarUseCases {
        return CalendarUseCases(
            repository = calendarRepository,
            setCalendarUseCase = setCalendarUseCase,
            getCreatedCalendarsUseCase = getCreatedCalendarsUseCase,
            getCreatedCalendarUseCase = getCreatedCalendarUseCase,
            getReceivedCalendarsUseCase = getReceivedCalendarsUseCase,
            addReceivedCalendarByCodeUseCase = addReceivedCalendarByCodeUseCase
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
    fun provideAddReceivedCalendarByCodeUseCase(
        calendarRepository: CalendarRepository
    ): AddReceivedCalendarByCodeUseCase {
        return AddReceivedCalendarByCodeUseCase(calendarRepository)
    }


    @Provides
    @Singleton
    fun provideGetCreatedCalendarsUseCase(
        calendarRepository: CalendarRepository
    ): GetCreatedCalendarsUseCase {
        return GetCreatedCalendarsUseCase(calendarRepository)
    }

    @Provides
    @Singleton
    fun provideGetCreatedCalendarUseCase(
        calendarRepository: CalendarRepository
    ): GetCreatedCalendarUseCase {
        return GetCreatedCalendarUseCase(calendarRepository)
    }


    @Provides
    @Singleton
    fun provideGetReceivedCalendarsUseCase(
        calendarRepository: CalendarRepository
    ): GetReceivedCalendarsUseCase {
        return GetReceivedCalendarsUseCase(calendarRepository)
    }

}