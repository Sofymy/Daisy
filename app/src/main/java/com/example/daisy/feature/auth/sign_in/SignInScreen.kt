package com.example.daisy.feature.auth.sign_in

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.ui.util.Constants
import com.example.daisy.ui.util.UiEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.Flow
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@Composable
fun SignInScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { viewModel.onEvent(SignInUserEvent.IsSignedId) }, lifecycleOwner = lifecycleOwner)
    HandleUiEvents(
        uiEvent = viewModel.uiEvent,
        onNavigateToHome = onNavigateToHome
    )

    when {
        state.isLoading -> LoadingContent()
        state.isSignedIn == true -> LaunchedEffect(Unit) { onNavigateToHome() }
        else -> SignInContent(
            state = state,
            onNavigateToRegister = onNavigateToRegister,
            onFieldChange = viewModel::onEvent,
            onSignInClick = { viewModel.onEvent(SignInUserEvent.SignIn) },
            onGoogleSignIn = { token, email -> viewModel.onEvent(SignInUserEvent.SignInWithGoogle(token, email)) }
        )
    }
}

@Composable
fun LoadingContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.loading))
            CircularProgressIndicator()
        }
    }
}

@Composable
fun SignInContent(
    state: SignInUiState,
    onNavigateToRegister: () -> Unit,
    onFieldChange: (SignInUserEvent) -> Unit,
    onSignInClick: () -> Unit,
    onGoogleSignIn: (String?, String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInForm(state, onFieldChange, onSignInClick)
        Spacer(modifier = Modifier.height(16.dp))
        SignInWithGoogleButton(onGoogleSignIn)
        SignInWithFingerprintButton()
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToRegister) {
            Text(stringResource(R.string.register))
        }
    }
}

@Composable
fun SignInWithFingerprintButton(

) {
    val context = LocalContext.current

    IconButton(onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            fingerprintAuthenticate(context){ result, message ->
                when(result){
                    true -> { Log.d("eeeeee", "true") }
                    false -> { Log.d("eeeeee", "false") }
                }
            }
        }
    }) {
        Icon(imageVector = Icons.Default.Person, contentDescription = null)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun fingerprintAuthenticate(
    context: android.content.Context,
    onAuthenticate: (Boolean, String) -> Unit
) {
    val executor = context.mainExecutor
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthenticate(true, "Successful authentication")
                Log.d("TAG", "Authentication successful!!!")
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onAuthenticate(false, errString.toString())
                Log.e("TAG", "onAuthenticationError + $errorCode + $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onAuthenticate(false, "Authentication failed.")
                Log.e("TAG", "onAuthenticationFailed")
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(/* resId = */ R.string.biometric_authentication))
        .setDescription(context.getString(R.string.place_your_finger_the_sensor_to_authenticate))
        .setNegativeButtonText(context.getString(R.string.cancel))
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        .build()

    biometricPrompt.authenticate(promptInfo)
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
            label = { Text(stringResource(R.string.email)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = state.password,
            onValueChange = { onFieldChange(SignInUserEvent.PasswordChanged(it)) },
            label = { Text(stringResource(R.string.password)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClickSignIn) {
            Text(stringResource(R.string.sign_in))
        }
    }
}

@Composable
fun SignInWithGoogleButton(
    onSignInWithGoogle: (String?, String?) -> Unit,
) {
    val context = LocalContext.current
    val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(Constants.SERVER_CLIENT_ID)
            .build()
    )

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            onSignInWithGoogle(account.result.idToken, account.result.email)
        } catch (e: ApiException) {
            Log.e("SignInWithGoogleButton", "Google sign-in failed", e)
        }
    }

    Button(onClick = { launcher.launch(googleSignInClient.signInIntent) }) {
        Text(stringResource(R.string.sign_in_with_google))
    }
}

@Composable
fun HandleLifecycleEvents(
    onResume: () -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun HandleUiEvents(
    uiEvent: Flow<UiEvent>,
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNavigateToHome()
                is UiEvent.Error -> Log.e("SignInContent", "Sign-in error: ${event.message}")
            }
        }
    }
}
