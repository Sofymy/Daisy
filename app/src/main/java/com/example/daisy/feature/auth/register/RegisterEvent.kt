package com.example.daisy.feature.auth.register

sealed class RegisterEvent {
    data class EmailChanged(val email: String): RegisterEvent()
    data class PasswordChanged(val password: String): RegisterEvent()
    data class ConfirmPasswordChanged(val password: String): RegisterEvent()
    object PasswordVisibilityChanged: RegisterEvent()
    object ConfirmPasswordVisibilityChanged: RegisterEvent()
    object Register: RegisterEvent()
}