package com.example.daisy.ui.common.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
){

    val brush = createStripeBrush(
        stripeColor = Purple,
        stripeWidth = 3.dp,
        stripeToGapRatio = 0.2f
    )

    Box(
        Modifier
            .offset(10.dp)
            .bounceClick(onClick)
            .padding(vertical = 10.dp)
    ) {
        Box(
            modifier
                .matchParentSize()
                .background(brush, RoundedCornerShape(30))
                .border(1.dp, Purple, RoundedCornerShape(30))
        )
        Box(
            modifier = modifier
                .graphicsLayer {
                    translationX = -20f
                    translationY = -20f
                }
                .clip(RoundedCornerShape(30))
                .background(Color.White)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .padding(10.dp),
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
    content: @Composable () -> Unit
){

    val brush = Brush.linearGradient(gradient)

    Box(
        Modifier
            .offset(10.dp)
            .bounceClick(onClick)
            .padding(start = 0.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Box(
            modifier
                .matchParentSize()
                .background(Color.White, RoundedCornerShape(30))
        )
        Box(
            modifier = modifier
                .graphicsLayer {
                    translationX = -20f
                    translationY = -20f
                }
                .clip(RoundedCornerShape(30))
                .background(brush)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .padding(10.dp),
            ) {
                content()
            }
        }
    }
}