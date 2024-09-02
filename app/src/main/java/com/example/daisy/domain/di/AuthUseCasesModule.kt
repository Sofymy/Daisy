package com.example.daisy.domain.di

import com.example.daisy.data.datasource.auth.AuthenticationRepository
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.domain.usecases.auth.GetUserUidUseCase
import com.example.daisy.domain.usecases.auth.IsSignedInUseCase
import com.example.daisy.domain.usecases.auth.LogoutUseCase
import com.example.daisy.domain.usecases.auth.RegisterUseCase
import com.example.daisy.domain.usecases.auth.ResetPasswordUseCase
import com.example.daisy.domain.usecases.auth.SignInUseCase
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
        authenticationRepository: AuthenticationRepository,
        getUserUidUseCase: GetUserUidUseCase,
        isSignedInUseCase: IsSignedInUseCase,
        signInUseCase: SignInUseCase,
        logoutUseCase: LogoutUseCase,
        registerUseCase: RegisterUseCase,
        signInWithGoogleUseCase: SignInWithGoogleUseCase,
        resetPasswordUseCase: ResetPasswordUseCase
    ): AuthUseCases {
        return AuthUseCases(
            repository = authenticationRepository,
            getUserUidUseCase = getUserUidUseCase,
            isSignedInUseCase = isSignedInUseCase,
            signInUseCase = signInUseCase,
            logoutUseCase = logoutUseCase,
            registerUseCase = registerUseCase,
            signInWithGoogleUseCase = signInWithGoogleUseCase,
            resetPasswordUseCase = resetPasswordUseCase
        )
    }

    @Provides
    @Singleton
    fun provideGetUserUidUseCase(
        authenticationRepository: AuthenticationRepository
    ): GetUserUidUseCase {
        return GetUserUidUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideIsLoggedInUseCase(
        authenticationRepository: AuthenticationRepository
    ): IsSignedInUseCase {
        return IsSignedInUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(
        authenticationRepository: AuthenticationRepository
    ): SignInUseCase {
        return SignInUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(
        authenticationRepository: AuthenticationRepository
    ): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        authenticationRepository: AuthenticationRepository
    ): LogoutUseCase {
        return LogoutUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authenticationRepository: AuthenticationRepository
    ): RegisterUseCase {
        return RegisterUseCase(authenticationRepository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(
        authenticationRepository: AuthenticationRepository
    ): ResetPasswordUseCase {
        return ResetPasswordUseCase(authenticationRepository)
    }
}