package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationRepository.register(email, password)
}