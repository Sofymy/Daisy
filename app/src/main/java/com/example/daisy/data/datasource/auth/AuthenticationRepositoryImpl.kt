package com.example.daisy.data.datasource.auth

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthenticationRepository {

    override val isSignedIn: Boolean get() = auth.currentUser != null

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun logout() = auth.signOut()

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            auth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { continuation.resume(Result.success(email)) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun signInWithGoogle(token: String): Result<String>{
        return suspendCancellableCoroutine { continuation ->
            val authCredential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(authCredential)
                .addOnSuccessListener { continuation.resume(Result.success(token))}
                .addOnFailureListener { continuation.resumeWithException(it) }

        }
    }

    override suspend fun register(email: String, password: String) = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(user?.email?.substringBefore('@'))
                    .build()
                user?.updateProfile(profileChangeRequest)
                continuation.resume(Unit)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }
}