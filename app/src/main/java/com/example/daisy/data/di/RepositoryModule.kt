package com.example.daisy.data.di

import com.example.daisy.data.repository.calendar.CalendarRepository
import com.example.daisy.data.repository.calendar.CalendarRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCalendarRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): CalendarRepository = CalendarRepositoryImpl(firebaseFirestore, firebaseAuth)


}