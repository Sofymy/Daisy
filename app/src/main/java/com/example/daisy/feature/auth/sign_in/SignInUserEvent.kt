package com.example.daisy.feature.auth.sign_in

sealed class SignInUserEvent {
    data class EmailChanged(val email: String): SignInUserEvent()
    data class PasswordChanged(val password: String): SignInUserEvent()
    data class SignInWithGoogle(val token: String?) : SignInUserEvent()
    data object PasswordVisibilityChanged: SignInUserEvent()
    data object SignIn: SignInUserEvent()
    data object IsSignedId: SignInUserEvent()
}