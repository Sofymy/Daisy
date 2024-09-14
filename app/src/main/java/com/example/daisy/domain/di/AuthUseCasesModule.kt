package com.example.daisy.domain.di

import com.example.daisy.data.datasource.auth.AuthenticationService
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.domain.usecases.auth.GetCurrentUserUseCase
import com.example.daisy.domain.usecases.auth.GetUserUidUseCase
import com.example.daisy.domain.usecases.auth.IsSignedInUseCase
import com.example.daisy.domain.usecases.auth.LogoutUseCase
import com.example.daisy.domain.usecases.auth.RegisterUseCase
import com.example.daisy.domain.usecases.auth.ResetPasswordUseCase
import com.example.daisy.domain.usecases.auth.SignInWithEmailAndPasswordUseCase
import com.example.daisy.domain.usecases.auth.SignInWithGoogleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCasesModule {

    @Provides
    @Singleton
    fun provideAuthUseCases(
        authenticationService: AuthenticationService,
        getUserUidUseCase: GetUserUidUseCase,
        isSignedInUseCase: IsSignedInUseCase,
        signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
        logoutUseCase: LogoutUseCase,
        registerUseCase: RegisterUseCase,
        signInWithGoogleUseCase: SignInWithGoogleUseCase,
        resetPasswordUseCase: ResetPasswordUseCase,
        getCurrentUserUseCase: GetCurrentUserUseCase
    ): AuthUseCases {
        return AuthUseCases(
            repository = authenticationService,
            getUserUidUseCase = getUserUidUseCase,
            isSignedInUseCase = isSignedInUseCase,
            signInWithEmailAndPasswordUseCase = signInWithEmailAndPasswordUseCase,
            logoutUseCase = logoutUseCase,
            registerUseCase = registerUseCase,
            signInWithGoogleUseCase = signInWithGoogleUseCase,
            resetPasswordUseCase = resetPasswordUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase
        )
    }

    @Provides
    @Singleton
    fun provideGetUserUidUseCase(
        authenticationService: AuthenticationService
    ): GetUserUidUseCase {
        return GetUserUidUseCase(authenticationService)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(
        authenticationService: AuthenticationService
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authenticationService)
    }

    @Provides
    @Singleton
    fun provideIsLoggedInUseCase(
        authenticationService: AuthenticationService
    ): IsSignedInUseCase {
        return IsSignedInUseCase(authenticationService)
    }

    @Provides
    @Singleton
    fun provideSignInWithEmailAndPasswordUseCase(
        authenticationService: AuthenticationService
    ): SignInWithEmailAndPasswordUseCase {
        return SignInWithEmailAndPasswordUseCase(authenticationService)
    }

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(
        authenticationService: AuthenticationService
    ): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(authenticationService)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        authenticationService: AuthenticationService
    ): LogoutUseCase {
        return LogoutUseCase(authenticationService)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authenticationService: AuthenticationService
    ): RegisterUseCase {
        return RegisterUseCase(authenticationService = authenticationService)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(
        authenticationService: AuthenticationService
    ): ResetPasswordUseCase {
        return ResetPasswordUseCase(authenticationService)
    }
}