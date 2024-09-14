package com.example.daisy.data.datasource.auth

import com.google.firebase.auth.FirebaseUser

interface AuthenticationService {

    suspend fun register(email: String, password: String)

    suspend fun logout()

    suspend fun userUid(): String

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>

    suspend fun signInWithGoogle(token: String, email: String): Result<String>

    val isSignedIn: Boolean
    suspend fun currentUser(): FirebaseUser?
}