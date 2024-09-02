package com.example.daisy.data.datasource.auth

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun register(email: String, password: String)

    suspend fun logout()

    suspend fun userUid(): String

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>

    suspend fun signInWithGoogle(token: String): Result<String>

    val isSignedIn: Boolean
}