package com.gabrieldrn.konstellation.style.highlight

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Rounded shape used by a composable highlighting a selected value on a chart.
 * TODO Arrows + coerce min width/height with arrow size.
 * TODO Make this shape customizable.
 */
class HighlightPopupShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        val arcCornerRadius = with(density) {
            24f.dp
                .toPx()
                .coerceAtMost(size.height / 2)
                .coerceAtMost(size.width / 2)
        }
        val arrowSize = with(density) { 8.dp.toPx() }
        reset()
        // Top left
        arcTo(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = arcCornerRadius * 2,
                bottom = arcCornerRadius * 2
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Top
        lineTo(size.width - arcCornerRadius, 0f)
        // Top right
        arcTo(
            rect = Rect(
                left = size.width - arcCornerRadius * 2,
                top = 0f,
                right = size.width,
                bottom = arcCornerRadius * 2
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Right
        lineTo(size.width, size.height - arcCornerRadius)
        // Bottom right
        arcTo(
            rect = Rect(
                left = size.width - arcCornerRadius * 2,
                top = size.height - arcCornerRadius * 2,
                right = size.width,
                bottom = size.height
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Bottom arrow
        lineTo((size.width / 2) + arrowSize, size.height)
        lineTo(size.width / 2, size.height + arrowSize)
        lineTo((size.width / 2) - arrowSize, size.height)
        lineTo(arcCornerRadius, size.height)
        // Bottom left
        arcTo(
            rect = Rect(
                left = 0f,
                top = size.height - arcCornerRadius * 2,
                right = arcCornerRadius * 2,
                bottom = size.height
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Left
        lineTo(0f, arcCornerRadius)
    })
}