package com.example.daisy.ui.common.for_later_use

import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.daisy.ui.common.elements.WavyShape
import com.example.daisy.ui.theme.gradient2


@Composable
fun OnboardingAurora() {

    val transition = rememberInfiniteTransition(label = "")
    val animatedProgress by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutQuart),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val brush = Brush.verticalGradient(
        colors = gradient2,
    )

    Box(
        Modifier
        .fillMaxHeight(0.2f)
        .fillMaxWidth()
        .clip(WavyShape(2.dp,10.dp*animatedProgress))
        .padding(top = 0.dp)
        .graphicsLayer {
            alpha = animatedProgress
        }
        .blur(30.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
        .background(brush))

}