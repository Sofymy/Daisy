package com.example.daisy.feature.auth.register

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.ui.common.brushes.createHorizontalStripeBrush
import com.example.daisy.ui.common.brushes.createVerticalStripeBrush
import com.example.daisy.ui.common.elements.PrimaryButton
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.common.elements.RectangleWithCutCorners
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.DarkBlue
import com.example.daisy.ui.theme.LightBlue
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient
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

    RegisterContent(
        state = state,
        onValueChange = viewModel::onEvent,
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
fun RegisterContent(
    state: RegisterUiState,
    onValueChange: (RegisterUserEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {

    LazyColumn(
    ) {
        item {
            AuthChecklistAnimation{
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(brush = Brush.linearGradient(gradient), fontWeight = FontWeight.Black)) {
                        append("SIGN UP TO-DO")
                    }
                }, Modifier.padding(start = 20.dp))
                AuthFormChecklistItem("Valid email", state.registerValidation.isEmailValid)
                AuthFormChecklistItem("Password length", state.registerValidation.hasPasswordLength)
                AuthFormChecklistItem("Passwords matching", state.registerValidation.isPasswordsMatching)
            }
        }
        item {
            RegisterFormAndButtons(
                state = state,
                onValueChange = onValueChange,
                onRegisterClick = onRegisterClick,
                onNavigateToSignIn = onNavigateToSignIn,
                enabled = !state.registerValidation.successful
            )
        }
    }
}

@Composable
fun RegisterFormAndButtons(
    state: RegisterUiState,
    onValueChange: (RegisterUserEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    enabled: Boolean
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
            Modifier
                .fillMaxSize()
        ) {
            RegisterForm(
                state = state,
                onValueChange = onValueChange
            )
            RegisterButtons(
                onRegisterClick = onRegisterClick,
                onNavigateToSignIn = onNavigateToSignIn,
                enabled = state.registerValidation.successful
            )
        }
    }

}

@Composable
fun AuthChecklistAnimation(
    content: @Composable () -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")


    val rotate by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(3000), repeatMode = RepeatMode.Reverse),
        label = ""
    )

    val brushHorizontal = createHorizontalStripeBrush(
        stripeColor = Color.Blue.copy(0.1f),
        stripeWidth = 4.dp,
        stripeToGapRatio = 0.04f
    )
    val brushVertical = createVerticalStripeBrush(
        stripeColor = Color.Red.copy(0.1f),
        stripeWidth = 40.dp,
        stripeToGapRatio = 1f
    )

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(start = 100.dp, end = 100.dp, top = 40.dp, bottom = 40.dp)
    ) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset(-rotate.dp, 0.dp)
                .graphicsLayer {
                    rotationZ = rotate
                }
                .aspectRatio(1f)
                .background(Color.White, RoundedCornerShape(2))
                .background(brushHorizontal, RoundedCornerShape(2))
                .background(brushVertical, RoundedCornerShape(2))
                .fillMaxWidth()
        )

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .offset(y = (3).dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .size(35.dp)
                )
                Box(
                    Modifier
                        .offset(y = (0).dp)
                        .clip(CircleShape)
                        .background(Blue)
                        .size(35.dp)
                )
                Box(
                    Modifier
                        .offset(y = (-12).dp)
                        .clip(CircleShape)
                        .background(DarkBlue)
                        .size(22.dp)
                )
                Box(
                    Modifier
                        .offset(y = (-15).dp)
                        .clip(CircleShape)
                        .background(LightBlue)
                        .size(22.dp)
                )
            }

            Column(
                Modifier
                    .offset(-rotate.dp, 0.dp)
                    .graphicsLayer {
                        rotationZ = rotate
                    }
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
fun AuthFormChecklistItem(
    text: String,
    isValid: Boolean
) {
    val color = animateColorAsState(
        targetValue = if(isValid) Color.LightGray else Color.Gray,
        animationSpec  = tween(
            durationMillis = 300,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    val dividerWidth = animateFloatAsState(targetValue = if(isValid) 1f else 0f,
        label = ""
    )

    Box(
        Modifier.padding(start = 25.dp, top = 8.dp)
    ) {
        Row {
            RegisterCheckmark(isChecked = isValid)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = text, color = color.value, textDecoration = if(isValid) TextDecoration.LineThrough else TextDecoration.None, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun RegisterButtons(
    onRegisterClick: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ){
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .padding(10.dp),
            enabled = enabled
        ) {
            Text(stringResource(R.string.register), color = Purple, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RegisterForm(
    state: RegisterUiState,
    onValueChange: (RegisterUserEvent) -> Unit
) {
    Column(
        Modifier.padding(top = 20.dp)
    ) {
        PrimaryTextField(
            icon = Icons.Default.AlternateEmail,
            placeholderText = "Enter your email",
            value = state.email,
            keyboardType = KeyboardType.Email,
            onValueChange = { onValueChange(RegisterUserEvent.EmailChanged(it)) },
            label = stringResource(id = R.string.email)
        )
        PrimaryTextField(
            value = state.password,
            onValueChange = { onValueChange(RegisterUserEvent.PasswordChanged(it)) },
            placeholderText = "Enter a password",
            label = stringResource(id = R.string.password),
            keyboardType = KeyboardType.Password,
            icon = Icons.Default.Password
        )
        PrimaryTextField(
            value = state.confirmPassword,
            onValueChange = { onValueChange(RegisterUserEvent.ConfirmPasswordChanged(it)) },
            placeholderText = "Re-enter your password",
            label = stringResource(R.string.confirm_password),
            keyboardType = KeyboardType.Password,
            icon = Icons.Default.Password
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun RegisterCheckmark(isChecked: Boolean) {
    val progress by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing), label = ""
    )

    Canvas(modifier = Modifier.size(20.dp)) {
        val strokeWidth = 5f

        drawRoundRect(
            color = Color.Gray,
            size = Size(size.width, size.height),
            cornerRadius = CornerRadius(10f),
            style = Stroke(strokeWidth)
        )
        val start = Offset(size.width * 0.2f, size.height * 0.55f)
        val middle = Offset(size.width * 0.4f, size.height * 0.75f)
        val end = Offset(size.width * 0.8f, size.height * 0.35f)

        val firstSegmentProgress = (progress * 2f).coerceAtMost(1f)
        val secondSegmentProgress = ((progress - 0.5f) * 2f).coerceAtLeast(0f)

        if (firstSegmentProgress > 0) {
            val firstSegmentEnd = Offset(
                lerp(start.x, middle.x, firstSegmentProgress),
                lerp(start.y, middle.y, firstSegmentProgress)
            )
            drawLine(
                color = Blue,
                start = start,
                end = firstSegmentEnd,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        if (secondSegmentProgress > 0) {
            val secondSegmentEnd = Offset(
                lerp(middle.x, end.x, secondSegmentProgress),
                lerp(middle.y, end.y, secondSegmentProgress)
            )
            drawLine(
                color = Blue,
                start = middle,
                end = secondSegmentEnd,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}
