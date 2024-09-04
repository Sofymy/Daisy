package com.example.daisy.feature.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.ui.util.UiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun RegisterScreen(
    onNavigateToSignIn: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleUiEvents(
        uiEvent = viewModel.uiEvent,
        onNavigateToSignIn = onNavigateToSignIn
    )

    RegisterForm(
        state = state,
        onFieldChange = viewModel::onEvent,
        onRegisterClick = { viewModel.onEvent(RegisterUserEvent.RegisterUser) },
        onNavigateToSignIn = onNavigateToSignIn
    )
}

@Composable
fun HandleUiEvents(
    uiEvent: Flow<UiEvent>,
    onNavigateToSignIn: () -> Unit
) {
    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNavigateToSignIn()
                is UiEvent.Error -> {
                }
            }
        }
    }
}

@Composable
fun RegisterForm(
    state: RegisterUiState,
    onFieldChange: (RegisterUserEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    Column {
        TextField(
            value = state.email,
            onValueChange = { onFieldChange(RegisterUserEvent.EmailChanged(it)) },
            label = { Text(stringResource(R.string.email)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = state.password,
            onValueChange = { onFieldChange(RegisterUserEvent.PasswordChanged(it)) },
            label = { Text(stringResource(R.string.password)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRegisterClick) {
            Text(stringResource(R.string.register))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToSignIn) {
            Text(stringResource(R.string.navigate_to_sign_in))
        }
    }
}
