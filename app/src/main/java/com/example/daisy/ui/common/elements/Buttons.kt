package com.example.daisy.ui.common.elements

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.daisy.ui.common.animations.bounceClick
import com.example.daisy.ui.common.brushes.createStripeBrush
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
){
    val roundedCornerShapePercent = 10
    val brush = createStripeBrush(
        stripeColor = Purple,
        stripeWidth = 4.dp,
        stripeToGapRatio = 0.2f
    )
    val borderWidth = 2.dp
    val backgroundColor = animateColorAsState(targetValue = if(enabled) Color.White else DarkGrey,
        label = ""
    )

    Box(
        modifier
            .bounceClick { if (enabled) onClick() }
    ) {
        Box(
            modifier
                .graphicsLayer {
                    translationX = 20f
                    translationY = 20f
                }
                .matchParentSize()
                .background(brush, RoundedCornerShape(roundedCornerShapePercent))
                .border(borderWidth, Purple, RoundedCornerShape(roundedCornerShapePercent))
        )
        Box(
            modifier = modifier
                .border(borderWidth, Purple, RoundedCornerShape(roundedCornerShapePercent))
                .clip(RoundedCornerShape(roundedCornerShapePercent))
                .background(Color.White)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .padding(20.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
){
    val roundedCornerShapePercent = 10
    val brush = Brush.linearGradient(gradient)
    val borderWidth = 2.dp

    Box(
        modifier
            .bounceClick { if (enabled) onClick() }
            .padding(bottom = 10.dp)
    ) {
        Box(
            modifier
                .graphicsLayer {
                    translationX = 20f
                    translationY = 20f
                }
                .matchParentSize()
                .background(Purple, RoundedCornerShape(roundedCornerShapePercent))
                .border(borderWidth, Purple, RoundedCornerShape(roundedCornerShapePercent))
        )
        Box(
            modifier = modifier
                .border(borderWidth, Purple, RoundedCornerShape(roundedCornerShapePercent))
                .clip(RoundedCornerShape(roundedCornerShapePercent))
                .background(DarkGrey)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .padding(20.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
fun TertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Box(
        modifier = modifier.clickable {
            onClick()
        }.border(1.dp, Color.White.copy(.5f), RoundedCornerShape(20)).padding(8.dp)
    ){
        content()
    }
}