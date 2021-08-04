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
 */
class HighlightPopupShape(
    private val position: HighlightPosition
) : Shape {

    var cornersRadius = 24f.dp
    var arrowSize = 8f.dp

    val suggestedMinWidth
        get() = if (position.isVertical) arrowSize * 2 else 0.dp

    val suggestedMinHeight
        get() = if (position.isHorizontal) arrowSize * 2 else 0.dp

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        val arrowSizePx = with(density) { arrowSize.toPx() }
        var cornerRadiusPx = with(density) {
            cornersRadius.toPx()
        }
        val minBorderLength = cornerRadiusPx * 2 + arrowSizePx * 2
        val len = if (position.isVertical) size.width else size.height
        val invLen = if (position.isVertical) size.height else size.width
        if (minBorderLength > len) {
            cornerRadiusPx = cornerRadiusPx.coerceAtMost((len - (2 * arrowSizePx)) / 2)
            if (cornerRadiusPx * 2 > invLen) {
                cornerRadiusPx = cornerRadiusPx.coerceAtMost(invLen / 2)
            }
        } else cornerRadiusPx = cornerRadiusPx.coerceAtMost(invLen / 2)
        reset()
        // Top left
        arcTo(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = cornerRadiusPx * 2,
                bottom = cornerRadiusPx * 2
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Top
        if (position == HighlightPosition.BOTTOM) {
            lineTo((size.width / 2) - arrowSizePx, 0f)
            lineTo(size.width / 2, -arrowSizePx)
            lineTo((size.width / 2) + arrowSizePx, 0f)
        }
        lineTo(size.width - cornerRadiusPx, 0f)
        // Top right
        arcTo(
            rect = Rect(
                left = size.width - cornerRadiusPx * 2,
                top = 0f,
                right = size.width,
                bottom = cornerRadiusPx * 2
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Right
        if (position == HighlightPosition.START) {
            lineTo(size.width, (size.height / 2) - arrowSizePx)
            lineTo(size.width + arrowSizePx, size.height / 2)
            lineTo(size.width, (size.height / 2) + arrowSizePx)
        }
        lineTo(size.width, size.height - cornerRadiusPx)
        // Bottom right
        arcTo(
            rect = Rect(
                left = size.width - cornerRadiusPx * 2,
                top = size.height - cornerRadiusPx * 2,
                right = size.width,
                bottom = size.height
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Bottom arrow
        if (position in arrayOf(HighlightPosition.POINT, HighlightPosition.TOP)) {
            lineTo((size.width / 2) + arrowSizePx, size.height)
            lineTo(size.width / 2, size.height + arrowSizePx)
            lineTo((size.width / 2) - arrowSizePx, size.height)
        }
        lineTo(cornerRadiusPx, size.height)
        // Bottom left
        arcTo(
            rect = Rect(
                left = 0f,
                top = size.height - cornerRadiusPx * 2,
                right = cornerRadiusPx * 2,
                bottom = size.height
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Left
        if (position == HighlightPosition.END) {
            lineTo(0f, (size.height / 2) + arrowSizePx)
            lineTo(- arrowSizePx, size.height / 2)
            lineTo(0f, (size.height / 2) - arrowSizePx)
        }
        lineTo(0f, cornerRadiusPx)
    })
}