package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationService.register(email, password)
}