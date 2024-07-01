package com.example.daisy.feature.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.data.utils.UiEvent

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    LoginScreenContent(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToHome = onNavigateToHome
    )
}

@Composable
fun LoginScreenContent(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        loginViewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Error -> {}
                is UiEvent.Loading -> {}
                is UiEvent.Success -> {
                    onNavigateToHome()
                }
            }
        }
    }

    Column {
        TextField(value = loginState.email, onValueChange = {
            loginViewModel.onEvent(LoginEvent.EmailChanged(it))
        })
        TextField(value = loginState.password, onValueChange = {
            loginViewModel.onEvent(LoginEvent.PasswordChanged(it))
        })

        Button(onClick = { loginViewModel.onEvent(LoginEvent.Login) }) {
            Text("Register")
        }
        Text(loginState.email)

        Button(onClick = { onNavigateToRegister() }) {
            Text("Nav to register")
        }
    }
}
