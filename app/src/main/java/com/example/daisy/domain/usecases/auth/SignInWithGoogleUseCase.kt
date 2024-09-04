package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(token: String, email: String) =
        authenticationService.signInWithGoogle(token, email)
}