package com.example.daisy.domain.di

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.domain.usecases.calendar.AddReceivedCalendarByCodeUseCase
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.domain.usecases.calendar.GetCalendarDrawingUseCase
import com.example.daisy.domain.usecases.calendar.GetCreatedCalendarDayUseCase
import com.example.daisy.domain.usecases.calendar.GetCreatedCalendarUseCase
import com.example.daisy.domain.usecases.calendar.GetCreatedCalendarsUseCase
import com.example.daisy.domain.usecases.calendar.GetReceivedCalendarDayUseCase
import com.example.daisy.domain.usecases.calendar.GetReceivedCalendarsUseCase
import com.example.daisy.domain.usecases.calendar.SaveDayModificationsUseCase
import com.example.daisy.domain.usecases.calendar.SaveModificationsUseCase
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
        getCreatedCalendarDayUseCase: GetCreatedCalendarDayUseCase,
        getReceivedCalendarsUseCase: GetReceivedCalendarsUseCase,
        getReceivedCalendarDayUseCase: GetReceivedCalendarDayUseCase,
        getCalendarDrawingUseCase: GetCalendarDrawingUseCase,
        addReceivedCalendarByCodeUseCase: AddReceivedCalendarByCodeUseCase,
        saveModificationsUseCase: SaveModificationsUseCase,
        saveDayModificationsUseCase: SaveDayModificationsUseCase
    ): CalendarUseCases {
        return CalendarUseCases(
            repository = calendarRepository,
            setCalendarUseCase = setCalendarUseCase,
            getCreatedCalendarsUseCase = getCreatedCalendarsUseCase,
            getCreatedCalendarUseCase = getCreatedCalendarUseCase,
            getCreatedCalendarDayUseCase = getCreatedCalendarDayUseCase,
            getReceivedCalendarDayUseCase = getReceivedCalendarDayUseCase,
            getReceivedCalendarsUseCase = getReceivedCalendarsUseCase,
            addReceivedCalendarByCodeUseCase = addReceivedCalendarByCodeUseCase,
            getCalendarDrawingUseCase = getCalendarDrawingUseCase,
            saveModificationsUseCase = saveModificationsUseCase,
            saveDayModificationsUseCase = saveDayModificationsUseCase,
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
    fun provideSaveModificationsUseCase(
        calendarRepository: CalendarRepository
    ): SaveModificationsUseCase {
        return SaveModificationsUseCase(calendarRepository)
    }

    @Provides
    @Singleton
    fun provideSaveDayModificationsUseCase(
        calendarRepository: CalendarRepository
    ): SaveDayModificationsUseCase {
        return SaveDayModificationsUseCase(calendarRepository)
    }

    @Provides
    @Singleton
    fun provideGetCalendarDrawingUseCase(
        calendarRepository: CalendarRepository
    ): GetCalendarDrawingUseCase {
        return GetCalendarDrawingUseCase(calendarRepository)
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
    fun provideGetCreatedCalendarDayUseCase(
        calendarRepository: CalendarRepository
    ): GetCreatedCalendarDayUseCase {
        return GetCreatedCalendarDayUseCase(calendarRepository)
    }


    @Provides
    @Singleton
    fun provideGetReceivedCalendarDayUseCase(
        calendarRepository: CalendarRepository
    ): GetReceivedCalendarDayUseCase {
        return GetReceivedCalendarDayUseCase(calendarRepository)
    }


    @Provides
    @Singleton
    fun provideGetReceivedCalendarsUseCase(
        calendarRepository: CalendarRepository
    ): GetReceivedCalendarsUseCase {
        return GetReceivedCalendarsUseCase(calendarRepository)
    }

}