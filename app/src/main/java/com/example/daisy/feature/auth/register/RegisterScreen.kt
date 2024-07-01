package com.example.daisy.feature.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterScreenContent(
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterScreenContent(
    onNavigateToLogin: () -> Unit
) {
    Column {
        Text("Register")
        Button(onClick = { onNavigateToLogin() }) {
            Text("Nav to login")
        }
    }
}