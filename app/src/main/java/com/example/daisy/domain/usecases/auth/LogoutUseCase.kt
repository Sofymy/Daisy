package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke() = authenticationService.logout()
}