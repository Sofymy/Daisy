package com.example.daisy.feature.auth.sign_in

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.feature.auth.register.AuthChecklistAnimation
import com.example.daisy.feature.auth.register.AuthFormChecklistItem
import com.example.daisy.ui.common.elements.PrimaryButton
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.common.elements.RectangleWithCutCorners
import com.example.daisy.ui.common.elements.SecondaryButton
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient
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
    val state by viewModel.state.collectAsStateWithLifecycle()

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
    LazyColumn() {
        item {
            AuthChecklistAnimation{
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(brush = Brush.linearGradient(gradient), fontWeight = FontWeight.Black)) {
                        append("SIGN IN TO-DO")
                    }
                }, Modifier.padding(start = 20.dp))
                AuthFormChecklistItem("Valid email", state.signInValidation.isEmailValid)
                AuthFormChecklistItem("Remember your super-secret password", state.signInValidation.isPasswordNotEmpty)
                AuthFormChecklistItem("Hit Sign in button", state.signInValidation.isSignInHit)
            }
        }
        item {
            SignInForm(state = state, onFieldChange = onFieldChange, onGoogleSignIn = onGoogleSignIn) {
                onSignInClick()
            }
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
    onGoogleSignIn: (String?, String?) -> Unit,
    onClickSignIn: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(MediumGrey, RectangleWithCutCorners())
    ) {
        Box(Modifier
            .fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val trianglePath = Path().let {
                    it.moveTo(this.size.width * .40f, 0f)
                    it.lineTo(this.size.width * .50f, -70f)
                    it.lineTo(this.size.width * .60f, 0f)
                    it.close()
                    it
                }
                drawPath(
                    path = trianglePath,
                    MediumGrey
                )
            }
        }
        Column(
            Modifier.padding(top = 20.dp)
        ) {
            PrimaryTextField(
                value = state.email,
                onValueChange = { onFieldChange(SignInUserEvent.EmailChanged(it)) },
                label = stringResource(R.string.email),
                icon = Icons.Default.AlternateEmail
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryTextField(
                value = state.password,
                keyboardType = KeyboardType.Password,
                onValueChange = { onFieldChange(SignInUserEvent.PasswordChanged(it)) },
                label = stringResource(R.string.password),
                icon = Icons.Default.Password
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(onClick = onClickSignIn, modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(stringResource(R.string.sign_in), color = Purple, fontWeight = FontWeight.Bold)
            }
            SignInWithGoogleButton(onGoogleSignIn)
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(top = 40.dp)
    ) {
        HorizontalDivider(color = Color.White, modifier = Modifier.padding(20.dp), thickness = 2.dp)
        Text("or", Modifier
            .background(MediumGrey)
            .padding(horizontal = 20.dp)
        )
    }
    SecondaryButton(
        onClick = { launcher.launch(googleSignInClient.signInIntent) },
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(stringResource(R.string.sign_in_with_google), fontWeight = FontWeight.Bold)
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
