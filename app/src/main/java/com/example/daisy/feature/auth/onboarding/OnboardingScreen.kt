package com.example.daisy.feature.auth.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.ui.common.elements.PrimaryButton
import com.example.daisy.ui.common.elements.WavyShape
import com.example.daisy.ui.common.for_later_use.toPx
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.theme.DaisyTheme
import com.example.daisy.ui.theme.LightPurple
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient
import com.example.daisy.ui.theme.gradient2
import kotlinx.coroutines.delay
import kotlin.random.Random

@Preview
@Composable
fun OnboardingContentPreview() {
    DaisyTheme {
        OnboardingContent(
            onNavigateToRegister = {},
            onNavigateToSignIn = {}
        )
    }
}

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { viewModel.onEvent(OnboardingUserEvent.IsSignedId) }
    )

    when {
        state.isLoading -> LoadingContent()
        state.isSignedIn == true -> LaunchedEffect(Unit) { onNavigateToHome() }
        else -> OnboardingContent(
            onNavigateToRegister = onNavigateToRegister,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}

@Composable
fun OnboardingContent(
    onNavigateToRegister: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val showContent = remember { mutableStateOf(false) }
    val showShootingStar = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContent.value = true
        delay(500)
        showShootingStar.value = true
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        OnboardingShootingStar(showShootingStar.value)
        OnboardingTextAndButtons(
            showContent = showContent.value,
            onNavigateToRegister = onNavigateToRegister,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}

@Composable
fun OnboardingShootingStar(
    value: Boolean
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val animateShootingStarX = animateIntAsState(targetValue = if(value) screenWidth.toPx().toInt()+30 else -30,
        label = "", animationSpec = tween(2000, easing = FastOutSlowInEasing)
    )
    val animateShootingStarY = animateIntAsState(targetValue = if(value) 400 else 0,
        label = "", animationSpec = tween(2000, easing = FastOutSlowInEasing)
    )

    Box(
        Modifier
            .offset { IntOffset(animateShootingStarX.value, animateShootingStarY.value) }
            .size(10.dp)
            .background(Color.White, CircleShape)
    )

}


@Composable
fun OnboardingTextAndButtons(
    showContent: Boolean,
    onNavigateToRegister: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val background = LightPurple

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp, 0.dp, 0.dp, 40.dp))
                .padding(start = 40.dp, end = 40.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            OnboardingTypewriterText(
                baseText = "Create ",
                highlightedText = "calendars\n",
                remainingText = "for ",
                parts = listOf("christmas.", "birthday.", "anniversary."),
                gradient = gradient
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
        }
        AnimatedVisibility(
            enter = scaleIn(
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                initialScale = 0f,
                transformOrigin = TransformOrigin(0.5f, 0.8f)
            ),
            visible = showContent
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(onClick = onNavigateToRegister, content = {
                    Text(
                        stringResource(R.string.register),
                        color = Purple,
                        fontWeight = FontWeight.Bold
                    )
                })
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.already_has_an_account_sign_in), Modifier.clickable { onNavigateToSignIn() })
            }
        }
    }
}

// SOURCE: https://blog.canopas.com/jetpack-compose-typewriter-animation-with-highlighted-texts-74397fee42f1

@Composable
fun OnboardingTypewriterText(
    baseText: String,
    highlightedText: String,
    remainingText: String,
    parts: List<String>,
    gradient: List<Color>
) {

    var partIndex by remember { mutableIntStateOf(0) }
    var partText by remember { mutableStateOf("") }

    val totalTextBeforePart = baseText.length + highlightedText.length + remainingText.length
    var partTextRects by remember { mutableStateOf(listOf<Rect>()) }

    LaunchedEffect(key1 = parts) {
        while (partIndex < parts.size) {
            val part = parts[partIndex]

            part.forEachIndexed { charIndex, _ ->
                partText = part.substring(startIndex = 0, endIndex = charIndex + 1)
                delay(100)
            }

            delay(1000)

            part.forEachIndexed { charIndex, _ ->
                partText = part.substring(startIndex = 0, endIndex = part.length - (charIndex + 1))
                delay(30)
            }

            delay(500)

            partIndex = (partIndex + 1) % parts.size
        }
    }

    Text(
        text = buildAnnotatedString {
            append(baseText)

            withStyle(SpanStyle(brush = Brush.linearGradient(gradient))) {
                append(highlightedText)
            }

            append(remainingText)
            append(partText)
        },
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            letterSpacing = -(1.6).sp,
            lineHeight = 60.sp
        ),
        modifier = Modifier
            .drawBehind {
                val borderSize = 20.sp.toPx()
                partTextRects.forEach { rect ->
                    val selectedRect = rect.translate(0f, -borderSize / 1.5f)
                    drawLine(
                        color = Purple,
                        start = Offset(selectedRect.left, selectedRect.bottom),
                        end = selectedRect.bottomRight,
                        strokeWidth = borderSize
                    )
                }
            },
        onTextLayout = { layoutResult ->
            val partTextEnd =
                totalTextBeforePart + partText.length.coerceAtMost(layoutResult.layoutInput.text.length)

            partTextRects =
                if (totalTextBeforePart <= partTextEnd && partTextEnd <= layoutResult.layoutInput.text.length) {
                    layoutResult.getBoundingBoxesForRange(
                        start = totalTextBeforePart,
                        end = partTextEnd -1
                    )
                } else {
                    emptyList()
                }
        }
    )
}


fun TextLayoutResult.getBoundingBoxesForRange(start: Int, end: Int): List<Rect> {
    var prevRect: Rect? = null
    var firstLineCharRect: Rect? = null
    val boundingBoxes = mutableListOf<Rect>()
    for (i in start..end) {
        val rect = getBoundingBox(i)
        val isLastRect = i == end

        if (isLastRect && firstLineCharRect == null) {
            firstLineCharRect = rect
            prevRect = rect
        }
        if (!isLastRect && rect.right == 0f) continue

        if (firstLineCharRect == null) {
            firstLineCharRect = rect
        } else if (prevRect != null) {
            if (prevRect.bottom.toFloat() != rect.bottom || isLastRect) {
                boundingBoxes.add(
                    firstLineCharRect.copy(right = prevRect.right)
                )
                firstLineCharRect = rect
            }
        }
        prevRect = rect
    }
    return boundingBoxes
}