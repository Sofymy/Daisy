package com.example.daisy.ui.common.brushes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Dp
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.Purple

fun createStripeBrush(
    stripeColor: Color,
    stripeWidth: Dp,
    stripeToGapRatio: Float
): Brush {
    val stripeWidthPx = stripeWidth.value
    val stripeGapWidthPx = stripeWidthPx / stripeToGapRatio
    val brushSizePx = stripeGapWidthPx + stripeWidthPx
    val stripeStart = stripeGapWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to Color.Transparent, stripeStart to stripeColor,
        start = Offset(0f, brushSizePx),
        end = Offset(brushSizePx, 0f),
        tileMode = TileMode.Repeated
    )
}