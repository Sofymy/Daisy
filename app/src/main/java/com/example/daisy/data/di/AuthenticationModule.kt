package com.example.daisy.data.di

import com.example.daisy.data.datasource.auth.AuthenticationRepository
import com.example.daisy.data.datasource.auth.AuthenticationRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesAuthenticationRepository(
        firebaseAuth: FirebaseAuth
    ): AuthenticationRepository = AuthenticationRepositoryImpl(firebaseAuth)

}