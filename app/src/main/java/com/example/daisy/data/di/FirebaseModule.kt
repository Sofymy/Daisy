package com.example.daisy.data.di

import com.example.daisy.data.datasource.auth.AuthenticationService
import com.example.daisy.data.datasource.auth.AuthenticationServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): StorageReference = FirebaseStorage.getInstance().reference

    @Provides
    @Singleton
    fun providesAuthenticationService(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthenticationService = AuthenticationServiceImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

}