package com.example.daisy.ui.util

import android.util.Patterns


data class RegisterValidation(
    val hasPasswordLength: Boolean = false,
    val isPasswordsMatching: Boolean = false,
    val isEmailValid: Boolean = false,
    val hasError: Boolean = true
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
            hasError = hasError
        )
    }
}

