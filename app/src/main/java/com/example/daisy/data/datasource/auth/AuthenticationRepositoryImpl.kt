package com.example.daisy.data.datasource.auth

import com.example.daisy.data.utils.UiEvent
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthenticationRepository {

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun isLoggedIn(): Boolean = auth.currentUser == null

    override suspend fun logout() = auth.signOut()

    override suspend fun login(email: String, password: String): Flow<UiEvent<AuthResult>> = flow {
        try {
            emit(UiEvent.Loading)
            val data = auth.signInWithEmailAndPassword(email, password).await()
            emit(UiEvent.Success(data))
        } catch (e: Exception) {
            emit(UiEvent.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }

    override suspend fun register(email: String, password: String): Flow<UiEvent<AuthResult>> =
        flow {
            try {
                emit(UiEvent.Loading)
                val data = auth.createUserWithEmailAndPassword(email, password).await()
                emit(UiEvent.Success(data))
            } catch (e: Exception) {
                emit(UiEvent.Error(e.localizedMessage ?: "Oops, something went wrong."))
            }
        }

    override suspend fun resetPassword(email: String): Flow<UiEvent<Void?>> = flow {
        try {
            emit(UiEvent.Loading)
            val data = auth.sendPasswordResetEmail(email).await()
            emit(UiEvent.Success(data))
        } catch (e: Exception) {
            emit(UiEvent.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }

}