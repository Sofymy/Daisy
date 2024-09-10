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

fun createHorizontalStripeBrush(
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
        end = Offset(0f, 0f),
        tileMode = TileMode.Repeated
    )
}

fun createVerticalStripeBrush(
    stripeColor: Color,
    stripeWidth: Dp,
    stripeToGapRatio: Float
): Brush {
    val stripeWidthPx = stripeWidth.value
    val stripeGapWidthPx = stripeWidthPx / stripeToGapRatio
    val brushSizePx = stripeGapWidthPx + stripeWidthPx
    val stripeStart = stripeGapWidthPx / brushSizePx

    return Brush.linearGradient(
        0.4f to Color.Transparent, stripeStart to stripeColor, stripeStart to Color.Transparent,
        start = Offset(brushSizePx, brushSizePx),
        end = Offset(0f, brushSizePx)
    )
}