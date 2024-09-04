package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUidUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke() = flow { emit(authenticationService.userUid()) }
}