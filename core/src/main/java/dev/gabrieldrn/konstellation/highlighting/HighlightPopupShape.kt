package dev.gabrieldrn.konstellation.highlighting

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Rounded shape used by a composable highlighting a selected value on a chart.
 * @property contentPosition Position of this popup in front of the chart.
 */
public open class HighlightPopupShape(
    private val contentPosition: HighlightContentPosition
) : Shape {

    /**
     * The size of all corners for this shape.
     */
    public open var cornersRadius: Dp = 16f.dp

    /**
     * The size of the arrow for this shape, as a distance from the arrow's head to shape border.
     */
    public open var arrowSize: Dp = 8f.dp

    /**
     * The suggested minimum width for this popup to be appropriately displayed.
     */
    public val suggestedMinWidth: Dp
        get() = if (contentPosition.isVertical) arrowSize * 2 else 0.dp

    /**
     * The suggested minimum height for this popup to be appropriately displayed.
     */
    public val suggestedMinHeight: Dp
        get() = if (contentPosition.isHorizontal) arrowSize * 2 else 0.dp

    /**
     * Creates the outline for this popup shape.
     */
    @Suppress("CognitiveComplexMethod") // This function stays legible enough.
    final override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(Path().apply {
        val arrowSizePx = with(density) { arrowSize.toPx() }
        var cornerRadiusPx = with(density) { cornersRadius.toPx() }
        val minBorderLength = cornerRadiusPx * 2 + arrowSizePx * 2
        val len = if (contentPosition.isVertical) size.width else size.height
        val invLen = if (contentPosition.isVertical) size.height else size.width
        val isOnTopOrOnPoint = contentPosition in arrayOf(
            HighlightContentPosition.Point, HighlightContentPosition.Top
        )
        if (minBorderLength > len) {
            cornerRadiusPx = cornerRadiusPx.coerceAtMost((len - 2 * arrowSizePx) / 2)
            if (cornerRadiusPx * 2 > invLen) {
                cornerRadiusPx = cornerRadiusPx.coerceAtMost(invLen / 2)
            }
        } else cornerRadiusPx = cornerRadiusPx.coerceAtMost(invLen / 2)
        reset()
        // Top left
        createCorner(Corner.TOP_LEFT, size, cornerRadiusPx)
        // Top
        if (contentPosition == HighlightContentPosition.Bottom) createTopArrow(size, arrowSizePx)
        lineTo(size.width - cornerRadiusPx, 0f) // linkage to next corner
        // Top right
        createCorner(Corner.TOP_RIGHT, size, cornerRadiusPx)
        // Right
        if (contentPosition == HighlightContentPosition.Start) createRightArrow(size, arrowSizePx)
        lineTo(size.width, size.height - cornerRadiusPx)
        // Bottom right
        createCorner(Corner.BOTTOM_RIGHT, size, cornerRadiusPx)
        // Bottom arrow
        if (isOnTopOrOnPoint) createBottomArrow(size, arrowSizePx)
        lineTo(cornerRadiusPx, size.height)
        // Bottom left
        createCorner(Corner.BOTTOM_LEFT, size, cornerRadiusPx)
        // Left
        if (contentPosition == HighlightContentPosition.End) createLeftArrow(size, arrowSizePx)
        lineTo(0f, cornerRadiusPx)
    })

    /**
     * On the receiving [Path], creates a corner depending on [corner] type.
     * @param corner Position enumerated in [Corner]
     * @param size The size of this shape boundaries.
     * @param cornerRadiusPx Computed corners size in pixels.
     */
    public open fun Path.createCorner(
        corner: Corner,
        size: Size,
        cornerRadiusPx: Float
    ): Unit = when (corner) {
        Corner.TOP_LEFT -> arcTo(
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
        Corner.TOP_RIGHT -> arcTo(
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
        Corner.BOTTOM_RIGHT -> arcTo(
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
        Corner.BOTTOM_LEFT -> arcTo(
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
    }

    /**
     * Creates an arrow on this [Shape], supposedly that it will be on the left side.
     * @param size The size of this shape boundaries.
     * @param arrowSizePx Computed arrow size in pixels.
     */
    public open fun Path.createLeftArrow(size: Size, arrowSizePx: Float) {
        lineTo(0f, size.height / 2 + arrowSizePx)
        lineTo(-arrowSizePx, size.height / 2)
        lineTo(0f, size.height / 2 - arrowSizePx)
    }

    /**
     * Creates an arrow on this [Shape], supposedly that it will be on the bottom side.
     * @param size The size of this shape boundaries.
     * @param arrowSizePx Computed arrow size in pixels.
     */
    public open fun Path.createBottomArrow(size: Size, arrowSizePx: Float) {
        lineTo(size.width / 2 + arrowSizePx, size.height)
        lineTo(size.width / 2, size.height + arrowSizePx)
        lineTo(size.width / 2 - arrowSizePx, size.height)
    }

    /**
     * Creates an arrow on this [Shape], supposedly that it will be on the right side.
     * @param size The size of this shape boundaries.
     * @param arrowSizePx Computed arrow size in pixels.
     */
    public open fun Path.createRightArrow(size: Size, arrowSizePx: Float) {
        lineTo(size.width, size.height / 2 - arrowSizePx)
        lineTo(size.width + arrowSizePx, size.height / 2)
        lineTo(size.width, size.height / 2 + arrowSizePx)
    }

    /**
     * Creates an arrow on this [Shape], supposedly that it will be on the top side.
     * @param size The size of this shape boundaries.
     * @param arrowSizePx Computed arrow size in pixels.
     */
    public open fun Path.createTopArrow(size: Size, arrowSizePx: Float) {
        lineTo(size.width / 2 - arrowSizePx, 0f)
        lineTo(size.width / 2, -arrowSizePx)
        lineTo(size.width / 2 + arrowSizePx, 0f)
    }

    /**
     * Enumeration of possible computed corner positions for this shape.
     */
    public enum class Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
