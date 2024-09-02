package com.example.daisy.feature.auth.register

sealed class RegisterUserEvent {
    data class EmailChanged(val email: String): RegisterUserEvent()
    data class PasswordChanged(val password: String): RegisterUserEvent()
    data class ConfirmPasswordChanged(val password: String): RegisterUserEvent()
    data object PasswordVisibilityChanged: RegisterUserEvent()
    data object ConfirmPasswordVisibilityChanged: RegisterUserEvent()
    data object RegisterUser: RegisterUserEvent()
}