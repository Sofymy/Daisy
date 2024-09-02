package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationRepository
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke() = authenticationRepository.isSignedIn
}