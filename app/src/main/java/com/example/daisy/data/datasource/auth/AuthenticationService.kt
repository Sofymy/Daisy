package com.example.daisy.data.datasource.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface AuthenticationService {

    suspend fun register(email: String, password: String)

    suspend fun signOut()

    suspend fun userUid(): String

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>

    suspend fun signInWithGoogle(token: String, email: String): Result<String>

    val isSignedIn: Boolean

    suspend fun currentUser(): FirebaseUser?

    suspend fun changeName(name: String): Result<Unit>

    suspend fun changePhotoUri(uri: Uri): Result<Unit>
}