package com.example.daisy.feature.auth.sign_in

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
    val isSignedIn: Boolean? = null
)