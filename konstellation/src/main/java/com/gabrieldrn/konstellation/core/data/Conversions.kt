package com.gabrieldrn.konstellation.core.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.core.plotting.xMax
import com.gabrieldrn.konstellation.core.plotting.xMin
import com.gabrieldrn.konstellation.core.plotting.xRange
import com.gabrieldrn.konstellation.core.plotting.yRange
import kotlin.math.abs

/**
 * Given two ranges A and B and a point at a position P1 inside A, this function will, from B,
 * return a position P2 which value is relative to the value of P1.
 *
 * Example:
 *
 * ```
 *  0.0    P1 = 250     500.0
 * A|---------x---------|
 * B|---------x---------|
 *  0.0    P2 = 0.5     1.0
 * ```
 *
 * Given a range A [[0.0; 500.0]] and a range B [[0.0;1.0]], if P1 = 250 then P2 = 0.5
 */
internal fun ClosedRange<Float>.convertPointToRange(
    initPoint: Float,
    targetRange: ClosedRange<Float>
): Float {
    require(initPoint in this) {
        "Initial point must be within the bounds of the initial range"
    }
    val offsetRange = if (targetRange.start < 0.0) abs(targetRange.start) else 0f
    return (((targetRange.endInclusive + offsetRange) - (targetRange.start + offsetRange))
            * (initPoint - start) / (endInclusive - start)
            ) - offsetRange
}

/**
 * TODO KDOC
 */
internal fun Collection<Point>.createOffsets(
    drawScope: DrawScope,
    yRange: ClosedFloatingPointRange<Float> = this.yRange
): Collection<Point> {
    val canvasYRange = drawScope.size.height..0f //Inverting to draw from bottom to top
    val canvasXRange = 0f..drawScope.size.width
    return map {
        it.copy(
            y = -it.y,
            offset = Offset(
                x = xRange.convertPointToRange(it.x, canvasXRange),
                y = yRange.convertPointToRange(it.y, canvasYRange) + drawScope.size.height,
            )
        )
    }
}

/**
 * Returns a position inside the given data range, relative to a given X position in the canvas.
 *
 * Example:
 *
 * Given a canvas width of 500 and data set with a range of [0.0;1.0]
 * If canvas x pos = 250
 * Then returned pos in dataset = 0.5
 *
 * @param canvasPos X position in the current canvas.
 * @param range Range of the dataset
 */
internal fun DrawScope.convertCanvasXToDataX(
    canvasPos: Int,
    range: ClosedFloatingPointRange<Float>
) = (0f..size.width).convertPointToRange(canvasPos.toFloat(), range)
