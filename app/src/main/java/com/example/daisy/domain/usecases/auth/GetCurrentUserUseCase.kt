package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke() = authenticationService.currentUser()
}