package com.example.daisy.data.datasource.auth

import android.net.Uri
import com.example.daisy.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.internal.wait
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

    override suspend fun changeName(name: String): Result<Unit>{
        return suspendCancellableCoroutine { continuation ->

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            auth.currentUser?.updateProfile(profileChangeRequest)
                ?.addOnSuccessListener {
                    continuation.resume(Result.success(Unit))
                }
                ?.addOnFailureListener { continuation.resumeWithException(it) }

        }
    }

    override suspend fun changePhotoUri(uri: Uri): Result<Unit>{
        return suspendCancellableCoroutine { continuation ->

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()

            auth.currentUser?.updateProfile(profileChangeRequest)
                ?.addOnSuccessListener {
                    continuation.resume(Result.success(Unit))
                }
                ?.addOnFailureListener { continuation.resumeWithException(it) }

        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

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
                    if(it.additionalUserInfo?.isNewUser == true) {
                        createUser(it.user?.let {
                            User(
                                uid = it.uid,
                                email = email,
                                name = it.displayName,
                                photoUrl = it.photoUrl.toString()
                            )
                        })
                    }
                    continuation.resume(Result.success(token))
                }
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

                createUser(user?.let { User(
                    uid = it.uid,
                    email = email,
                    name = it.displayName,
                    photoUrl = it.photoUrl.toString()
                ) })
                continuation.resume(Unit)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    private fun createUser(user: User?){
        if (user != null) {
            firestore.collection("users").document(user.uid)
                .set(user, SetOptions.merge())
        }
    }
}