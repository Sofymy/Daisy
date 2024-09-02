package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(token: String) =
        authenticationRepository.signInWithGoogle(token)
}