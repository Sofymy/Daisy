package com.example.daisy.feature.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.data.utils.UiEvent

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
) {
    RegisterScreenContent(
        onNavigateToLogin = onNavigateToLogin
    )

}

@Composable
fun RegisterScreenContent(
    onNavigateToLogin: () -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val registerState by registerViewModel.registerState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        registerViewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Error -> {}
                is UiEvent.Loading -> {}
                is UiEvent.Success -> {
                    onNavigateToLogin()
                }
            }
        }
    }

    Column {
        TextField(value = registerState.email, onValueChange = {
            registerViewModel.onEvent(RegisterEvent.EmailChanged(it))
        })
        TextField(value = registerState.password, onValueChange = {
            registerViewModel.onEvent(RegisterEvent.PasswordChanged(it))
        })

        Button(onClick = { registerViewModel.onEvent(RegisterEvent.Register) }) {
            Text("Register")
        }
        Text(registerState.email)

        Button(onClick = { onNavigateToLogin() }) {
            Text("Nav to login")
        }
    }
}