package com.example.daisy.feature.auth.login

sealed class LoginEvent {
    data class EmailChanged(val email: String): LoginEvent()
    data class PasswordChanged(val password: String): LoginEvent()
    object PasswordVisibilityChanged: LoginEvent()
    object Login: LoginEvent()
    object Has: LoginEvent()
}