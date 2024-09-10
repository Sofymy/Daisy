package com.example.daisy.ui.util

import android.util.Patterns


data class RegisterValidation(
    val hasPasswordLength: Boolean = false,
    val isPasswordsMatching: Boolean = false,
    val isEmailValid: Boolean = false,
    val successful: Boolean = false
)


data class SignInValidation(
    val isPasswordNotEmpty: Boolean = false,
    val isEmailValid: Boolean = false,
    val isSignInHit: Boolean = false,
    val successful: Boolean = false
)

class ValidateRegister {
    private fun validateEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validatePasswordsMatching(password: String, passwordConfirm: String): Boolean =
        password.isNotBlank() && password == passwordConfirm

    private fun validatePasswordLength(password: String): Boolean =
        password.length >= 6

    fun execute(password: String, email: String, passwordConfirm: String): RegisterValidation {

        val validPasswordLength = validatePasswordLength(password)
        val validEmail = validateEmail(email)
        val validPasswords = validatePasswordsMatching(password, passwordConfirm)

        val hasError = listOf(
            validPasswordLength,
            validEmail,
            validPasswords
        ).all { it }

        return RegisterValidation(
            hasPasswordLength = validPasswordLength,
            isEmailValid = validEmail,
            isPasswordsMatching = validPasswords,
            successful = hasError
        )
    }
}

class ValidateSignIn {
    private fun validateEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validatePasswordNotEmpty(password: String): Boolean =
        password.isNotBlank()

    private fun validateSignInHit(): Boolean =
        false

    fun execute(password: String, email: String): SignInValidation {

        val validPasswordNotEmpty = validatePasswordNotEmpty(password)
        val validEmail = validateEmail(email)
        val validSignInHit = validateSignInHit()

        val hasError = listOf(
            validPasswordNotEmpty,
            validEmail,
            validSignInHit
        ).all { it }

        return SignInValidation(
            isPasswordNotEmpty = validPasswordNotEmpty,
            isEmailValid = validEmail,
            isSignInHit = validSignInHit,
            successful = hasError
        )
    }
}

