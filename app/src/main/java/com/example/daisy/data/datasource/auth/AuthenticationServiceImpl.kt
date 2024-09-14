package com.example.daisy.data.datasource.auth

import com.example.daisy.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthenticationServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationService {

    override val isSignedIn: Boolean get() = auth.currentUser != null

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun currentUser(): FirebaseUser? = auth.currentUser

    override suspend fun logout() = auth.signOut()

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            auth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { continuation.resume(Result.success(email)) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun signInWithGoogle(token: String, email: String): Result<String>{
        return suspendCancellableCoroutine { continuation ->
            val authCredential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(authCredential)
                .addOnSuccessListener { it ->
                    createUser(it.user?.let { User(it.uid, email) })
                    continuation.resume(Result.success(token))
                }
                .addOnFailureListener { continuation.resumeWithException(it) }

        }
    }

    override suspend fun register(email: String, password: String) = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                createUser(result.user?.let { User(it.uid, email) })
                val user = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(user?.email?.substringBefore('@'))
                    .build()
                user?.updateProfile(profileChangeRequest)
                continuation.resume(Unit)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    private fun createUser(user: User?){
        if (user != null) {
            firestore.collection("users").document(user.uid).set(user, SetOptions.merge())
        }

    }
}