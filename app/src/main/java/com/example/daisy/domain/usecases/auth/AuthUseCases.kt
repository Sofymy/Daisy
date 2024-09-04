package com.example.daisy.domain.usecases.auth

import com.example.daisy.data.datasource.auth.AuthenticationService

class AuthUseCases(
    val repository: AuthenticationService,
    val getUserUidUseCase: GetUserUidUseCase,
    val isSignedInUseCase: IsSignedInUseCase,
    val signInUseCase: SignInUseCase,
    val logoutUseCase: LogoutUseCase,
    val registerUseCase: RegisterUseCase,
    val resetPasswordUseCase: ResetPasswordUseCase,
    val signInWithGoogleUseCase: SignInWithGoogleUseCase
)