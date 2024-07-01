package com.example.daisy.data.datasource.auth

import com.example.daisy.data.utils.UiEvent
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): Flow<UiEvent<AuthResult>>

    suspend fun register(email: String, password: String): Flow<UiEvent<AuthResult>>

    suspend fun resetPassword(email: String): Flow<UiEvent<Void?>>

    suspend fun logout()

    suspend fun userUid(): String

    suspend fun isLoggedIn(): Boolean
}