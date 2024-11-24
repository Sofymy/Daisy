package com.example.daisy.ui.common.for_later_use

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.daisy.R
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.Purple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun FallingObjects(){
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
            finalY = screenHeight - (giftSize.dp * 2),
            size = balloonSize.dp
        )
    }
    val balloonTranslatedY = remember { Animatable(0f) }
    var balloonIsClicked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        showContent.value = true
    }

    if(showContent.value)
    Box(modifier = Modifier
        .fillMaxWidth().fillMaxHeight(.7f)) {


        OnboardingFallingBalloon(
            balloon = balloon,
            isClickedBalloon = balloonIsClicked,
            onClickBalloon = { balloonIsClicked = balloonIsClicked.not() },
            screenHeight = screenHeight,
            translatedY = balloonTranslatedY,
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
