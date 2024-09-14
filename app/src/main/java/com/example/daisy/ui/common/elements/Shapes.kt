package com.example.daisy.ui.common.elements
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.ceil


class TrapezoidShapeUpper : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(
        Path().apply {
            moveTo(size.width * 0f, 0f) // Top left
            lineTo(size.width * 0.75f, 0f) // Top right
            lineTo(size.width, size.height) // Bottom right
            lineTo(0f, size.height) // Bottom left
            close()
        }
    )
}

class TrapezoidShapeLower(radius: Float) : Shape {

    private val _radius = radius
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(
        Path().apply {
            moveTo(size.width * 0f, 0f) // Top left
            lineTo(size.width, 0f) // Top right
            lineTo(size.width * _radius, size.height * 1f) // Bottom right
            lineTo(0f, size.height) // Bottom left
            close()
        }
    )
}

class RectangleWithCutCorners : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        val cutSize = 0.05f

        val path = Path().apply {
            moveTo(size.width * cutSize, 0f)

            lineTo(size.width * (1-cutSize), 0f)
            lineTo(size.width, size.height * cutSize)

            lineTo(size.width, size.height * (1-cutSize))
            lineTo(size.width * (1-cutSize), size.height)

            lineTo(size.width * cutSize, size.height)
            lineTo(0f, size.height * (1-cutSize))

            lineTo(0f, size.height * cutSize)
            close()
        }

        return Outline.Generic(path)
    }
}

class WavyShape(
    private val period: Dp,
    private val amplitude: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(Path().apply {

        val wavyPath = Path().apply {
            val halfPeriod = with(density) { period.toPx() } / 2
            val amplitude = with(density) { amplitude.toPx() }
            moveTo(x = -halfPeriod / 2, y = amplitude)
            repeat(ceil(size.width / halfPeriod + 1).toInt()) { i ->
                relativeQuadraticBezierTo(
                    dx1 = halfPeriod / 2,
                    dy1 = 2 * amplitude * (if (i % 2 == 0) 1 else -1),
                    dx2 = halfPeriod,
                    dy2 = 0f,
                )
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
        }
        val boundsPath = Path().apply {
            addRect(Rect(offset = Offset.Zero, size = size))
        }
        op(wavyPath, boundsPath, PathOperation.Intersect)
    })
}
