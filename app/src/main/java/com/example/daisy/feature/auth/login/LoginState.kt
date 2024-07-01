package com.example.daisy.feature.auth.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
    val isSignedIn: Boolean = false
)