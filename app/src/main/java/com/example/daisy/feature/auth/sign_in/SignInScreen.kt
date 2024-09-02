package com.example.daisy.feature.auth.sign_in

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.ui.util.Constants
import com.example.daisy.ui.util.UiEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.Flow

@Composable
fun SignInScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = state.isSignedIn == null

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(SignInUserEvent.IsSignedId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when {
        isLoading -> LoadingScreen()
        state.isSignedIn == true -> LaunchedEffect(Unit) { onNavigateToHome() }
        else -> SignInContent(
            onNavigateToRegister = onNavigateToRegister,
            onNavigateToHome = onNavigateToHome,
            uiEvent = viewModel.uiEvent,
            signInState = state,
            onSignInEvent = { event -> viewModel.onEvent(event) }
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Signing in...")
            CircularProgressIndicator()
        }
    }
}

@Composable
fun SignInContent(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    uiEvent: Flow<UiEvent<Any>>,
    signInState: SignInUiState,
    onSignInEvent: (SignInUserEvent) -> Unit
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNavigateToHome()
                is UiEvent.Error -> Log.e("SignInContent", "Sign-in error: ${event.message}")
                is UiEvent.Loading -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInForm(
            state = signInState,
            onFieldChange = { onSignInEvent(it) },
            onClickSignIn = { onSignInEvent(SignInUserEvent.SignIn) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SignInWithGoogleButton { token ->
            onSignInEvent(SignInUserEvent.SignInWithGoogle(token))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToRegister) {
            Text("Register")
        }
    }
}

@Composable
fun SignInForm(
    state: SignInUiState,
    onFieldChange: (SignInUserEvent) -> Unit,
    onClickSignIn: () -> Unit
) {
    Column {
        TextField(
            value = state.email,
            onValueChange = { onFieldChange(SignInUserEvent.EmailChanged(it)) },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = state.password,
            onValueChange = { onFieldChange(SignInUserEvent.PasswordChanged(it)) },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClickSignIn) {
            Text("Sign in")
        }
    }
}

@Composable
fun SignInWithGoogleButton(
    onClickSignInWithGoogleButton: (String?) -> Unit
) {
    val context = LocalContext.current
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Constants.SERVER_CLIENT_ID)
                .build()
        )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            onClickSignInWithGoogleButton(account.result.idToken)
        } catch (e: ApiException) {
            Log.e("SignInWithGoogleButton", "Google sign-in failed", e)
        }
    }

    Button(onClick = { launcher.launch(googleSignInClient.signInIntent) }) {
        Text("Sign in with Google")
    }
}
