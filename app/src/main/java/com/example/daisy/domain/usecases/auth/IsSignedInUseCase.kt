package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    operator fun invoke() = authenticationService.isSignedIn
}