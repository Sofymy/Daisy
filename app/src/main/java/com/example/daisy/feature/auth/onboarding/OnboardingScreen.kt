package com.example.daisy.feature.auth.onboarding

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.R
import com.example.daisy.ui.common.elements.PrimaryButton
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.theme.DaisyTheme
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
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
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val showContent = remember { mutableStateOf(false) }

    val giftSize = Random.nextInt(70, 120)
    val gift = remember {
        FallingGiftState(
            color = Color.White,
            startX = Random.nextInt(giftSize, screenWidth.value.toInt()).dp-giftSize.dp,
            initialY = (-giftSize).dp - 20.dp,
            finalY = screenHeight - giftSize.dp,
            size = giftSize.dp
        )
    }

    var giftIsOpen by remember { mutableStateOf(false) }
    val giftTop = remember { Animatable(0f) }
    val giftTranslatedX = remember { Animatable(0f) }
    val giftTranslatedY = remember { Animatable(0f) }

    val balloonSize = Random.nextInt(70, 120)
    val balloon = remember {
        FallingGiftState(
            color = Purple,
            startX = Random.nextInt(balloonSize, screenWidth.value.toInt()).dp-balloonSize.dp,
            initialY = (-100).dp,
            finalY = screenHeight - giftSize.dp,
            size = balloonSize.dp
        )
    }
    val balloonTranslatedY = remember { Animatable(0f) }
    var balloonIsClicked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContent.value = true
    }

    Box(modifier = Modifier
        .fillMaxSize()) {
        OnboardingFallingGift(
            gift = gift,
            isOpenGift = giftIsOpen,
            onChangeGiftState = { giftIsOpen = giftIsOpen.not() },
            rotatedGiftTop = giftTop,
            translatedX = giftTranslatedX,
            translatedY = giftTranslatedY,
        )

        OnboardingFallingBalloon(
            balloon = balloon,
            isClickedBalloon = balloonIsClicked,
            onClickBalloon = { balloonIsClicked = balloonIsClicked.not() },
            screenHeight = screenHeight,
            translatedY = balloonTranslatedY,
        )

        OnboardingTextAndButtons(
            showContent = showContent.value,
            onNavigateToRegister = onNavigateToRegister,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}

@Composable
fun OnboardingFallingBalloon(
    balloon: FallingGiftState,
    translatedY: Animatable<Float, AnimationVector1D>,
    onClickBalloon: () -> Unit,
    isClickedBalloon: Boolean,
    screenHeight: Dp
) {
    val coroutineScope = rememberCoroutineScope()

    val fallingAnimation = remember { Animatable(balloon.initialY.value) }

    val targetY = if (isClickedBalloon) -screenHeight.toPx() + balloon.size.toPx() + 100 else -balloon.size.toPx() / 2



    LaunchedEffect(balloon) {
        coroutineScope.launch {
            fallingAnimation.animateTo(
                targetValue = balloon.finalY.value,
                animationSpec = tween(durationMillis = (5000..9000).random(), easing = LinearOutSlowInEasing, delayMillis = 1000)
            )
        }
    }


    LaunchedEffect(isClickedBalloon) {
        coroutineScope.launch {
            translatedY.animateTo(
                targetValue = targetY,
                animationSpec = tween(durationMillis = 5000, easing = FastOutSlowInEasing)
            )
        }
    }

    OnboardingBalloonBox(
        gift = balloon,
        fallingAnimation = fallingAnimation,
        translatedY = translatedY,
        onClick = { onClickBalloon() }
    )
}

@Composable
fun OnboardingBalloonBox(
    gift: FallingGiftState,
    fallingAnimation: Animatable<Float, AnimationVector1D>,
    translatedY: Animatable<Float, AnimationVector1D>,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .size(gift.size)
            .offset { IntOffset(gift.startX.roundToPx(), fallingAnimation.value.dp.roundToPx()) }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.heart_balloon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gift.color),
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    translationY = translatedY.value
                }
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null,
                    onClick = onClick
                )
        )
    }
}

@Composable
fun OnboardingFallingGift(
    gift: FallingGiftState,
    isOpenGift: Boolean,
    onChangeGiftState: (Boolean) -> Unit,
    rotatedGiftTop: Animatable<Float, AnimationVector1D>,
    translatedX: Animatable<Float, AnimationVector1D>,
    translatedY: Animatable<Float, AnimationVector1D>
) {
    Log.d("eeeeeee", isOpenGift.toString())
    val coroutineScope = rememberCoroutineScope()

    val startFireworkInit = remember {
        mutableStateOf(false)
    }
    val startFireworkParticles = remember {
        mutableStateOf(false)
    }
    val finishedFireworkInit = {
        startFireworkParticles.value = true
    }
    val fallingAnimation = remember { Animatable(gift.initialY.value) }
    val animatedOffset = animateFloatAsState(targetValue = if(startFireworkInit.value) -(gift.size.value/2f) else -2.5f, finishedListener = { finishedFireworkInit() },
        label = ""
    )

    val targetY = if (isOpenGift) -gift.size.value / 2 else 0f
    val targetX = if (isOpenGift) gift.size.value else 0f
    val fireworkInitiated: Boolean = isOpenGift
    val fireworkInitColor = animateColorAsState(targetValue = if (startFireworkParticles.value) Color.Transparent else Color.White,
        label = ""
    )


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fallingAnimation.animateTo(
                targetValue = gift.finalY.value,
                animationSpec = tween(durationMillis = (5000..9000).random(), easing = EaseOutCubic, delayMillis = 1000)
            )
        }
    }

    LaunchedEffect(isOpenGift){
        coroutineScope.launch {
            translatedY.animateTo(
                targetValue = targetY,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
            translatedX.animateTo(
                targetValue = targetX,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
            startFireworkInit.value = fireworkInitiated
        }
    }

    LaunchedEffect(startFireworkParticles.value) {
        if(startFireworkParticles.value){
            delay(3000)
            onChangeGiftState(false)
        }
    }

    if(startFireworkInit.value && isOpenGift){
        Box(
            Modifier
                .offset(
                    gift.startX + gift.size / 2 - 2.5.dp,
                    gift.finalY + gift.size / 2 + animatedOffset.value.dp
                )
                .background(fireworkInitColor.value, CircleShape)
                .size(6.dp)) {
        }
    }

    if (startFireworkParticles.value){
        OnboardingFireworkEffect(
            triggerPoint = Offset(gift.startX.toPx() + gift.size.toPx() / 2, gift.finalY.toPx())
        )
    }

    OnboardingGiftBox(
        gift = gift,
        fallingAnimation = fallingAnimation,
        rotatedGiftTop = rotatedGiftTop,
        translatedX = translatedX,
        translatedY = translatedY,
        onClick = onChangeGiftState
    )
}


@Composable
fun Dp.toPx(): Float {
    val density = LocalContext.current.resources.displayMetrics.density
    return this.value * density
}

@Composable
fun OnboardingTextAndButtons(
    showContent: Boolean,
    onNavigateToRegister: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.padding(start = 40.dp, end = 40.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            OnboardingTypewriterText(
                baseText = "Create ",
                highlightedText = "calendars\n",
                remainingText = "for ",
                parts = listOf("christmas.", "birthday.", "anniversary."),
                gradient = gradient
            )
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
                    .padding(start = 40.dp, end = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                PrimaryButton(onClick = onNavigateToRegister) {
                    Text(
                        stringResource(R.string.register),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.already_has_an_account_sign_in), Modifier.clickable { onNavigateToSignIn() })
            }
        }
    }
}


@Composable
fun OnboardingGiftBox(
    gift: FallingGiftState,
    fallingAnimation: Animatable<Float, AnimationVector1D>,
    rotatedGiftTop: Animatable<Float, AnimationVector1D>,
    translatedX: Animatable<Float, AnimationVector1D>,
    translatedY: Animatable<Float, AnimationVector1D>,
    onClick: (Boolean) -> Unit
) {
    Column(
        Modifier
            .size(gift.size)
            .offset { IntOffset(gift.startX.roundToPx(), fallingAnimation.value.dp.roundToPx()) }
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = { onClick(true) }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.gift_top),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gift.color),
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    rotationZ = rotatedGiftTop.value
                    translationX = translatedX.value * 1.5f
                    translationY = translatedY.value / 2
                }
        )
        Image(
            painter = painterResource(R.drawable.gift_bottom),
            contentDescription = null,
            Modifier.weight(1f),
            colorFilter = ColorFilter.tint(gift.color)
        )
    }
}

@Composable
fun OnboardingFireworkEffect(
    modifier: Modifier = Modifier,
    triggerPoint: Offset,
    particlesCount: Int = 30,
    durationMillis: Int = 3000
) {
    val scope = rememberCoroutineScope()
    var particles by remember { mutableStateOf(emptyList<FireworkParticle>()) }
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(triggerPoint) {
        particles = generateFireworkParticles(triggerPoint, particlesCount)
        scope.launch { animatable.animateTo(1f, animationSpec = tween(durationMillis)) }
        delay(durationMillis.toLong())
        particles = emptyList()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { drawFireworkParticle(it, animatable.value) }
    }
}

fun generateFireworkParticles(triggerPoint: Offset, particlesCount: Int): List<FireworkParticle> {
    return List(particlesCount) {
        val angle = (360 * it / particlesCount) * (Math.PI / 180)
        FireworkParticle(
            initialPosition = triggerPoint,
            velocity = Offset(cos(angle).toFloat(), sin(angle).toFloat()) * Random.nextInt(0, 120)
                .toFloat(),
            color = listOf(Purple, Color.White, Blue).random()
        )
    }
}

fun DrawScope.drawFireworkParticle(particle: FireworkParticle, animProgress: Float) {
    val position = particle.initialPosition + particle.velocity * animProgress
    drawCircle(particle.color, radius = 2.dp.toPx(), center = position)
}

data class FireworkParticle(
    val initialPosition: Offset,
    val velocity: Offset,
    val color: Color
)

data class FallingGiftState(
    val color: Color,
    val startX: Dp,
    val initialY: Dp,
    val finalY: Dp,
    val size: Dp
)


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