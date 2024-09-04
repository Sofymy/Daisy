package com.example.daisy.feature.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.ui.util.UiEvent

@Composable
fun RegisterScreen(
    onNavigateToSignIn: () -> Unit,
) {
    RegisterContent(
        onNavigateToSignIn = onNavigateToSignIn
    )

}

@Composable
fun RegisterContent(
    onNavigateToSignIn: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Success -> {
                    onNavigateToSignIn()
                }
                is UiEvent.Error -> {

                }
            }
        }
    }

    RegisterForm(
        state = state,
        onFieldChange = { viewModel.onEvent(it) },
        onClickRegister = { viewModel.onEvent(RegisterUserEvent.RegisterUser) }
    )

}

@Composable
fun RegisterForm(
    state: RegisterUiState,
    onFieldChange: (RegisterUserEvent) -> Unit,
    onClickRegister: () -> Unit
) {

    Column {
        TextField(
            value = state.email,
            onValueChange = {
                onFieldChange(RegisterUserEvent.EmailChanged(it))
            }
        )
        TextField(
            value = state.password,
            onValueChange = {
                onFieldChange(RegisterUserEvent.PasswordChanged(it))
            }
        )

        Button(onClick = { onClickRegister() }) {
            Text("Register")
        }

        Button(onClick = {
            //onNavigateToLogin()
        }) {
            Text("Nav to login")
        }
    }

}
