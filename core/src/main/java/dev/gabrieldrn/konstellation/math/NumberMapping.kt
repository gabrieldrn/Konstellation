package dev.gabrieldrn.konstellation.math

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.Point
import dev.gabrieldrn.konstellation.plotting.xRange
import dev.gabrieldrn.konstellation.plotting.yRange
import dev.gabrieldrn.konstellation.util.distance

/**
 * Linearly maps a number that falls inside [fromRange] to [toRange].
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
 * Given a range A [[0.0; 500.0]] and a range B [[0.0;1.0]], if P1 = 250 then P2 = 0.5.
 *
 * @receiver The value to be mapped to the targeted range.
 * @param fromRange Initial range of the receiver.
 * @param toRange Targeted range.
 * @return The value converted with the targeted range.
 */
public fun Float.map(
    fromRange: ClosedFloatingPointRange<Float>,
    toRange: ClosedFloatingPointRange<Float>
): Float =
    // ratio = (value - from.start) / (from.end - from.start)
    toRange.start + (this - fromRange.start) / fromRange.distance * toRange.distance

/**
 * Infix function for [map], offering a more explicit usage of the latter.
 *
 * For example, `10f map (0f..20f to 0f..40f)`, linearly maps the number 10 from the range
 * [[0, 20]] to [[0, 40]].
 */
public infix fun Float.map(
    to: Pair<ClosedFloatingPointRange<Float>, ClosedFloatingPointRange<Float>>
): Float = this@map.map(to.first, to.second)

/**
 * Sets the offset attribute of each [Point] of the receiving collection, based on a given canvas
 * [size]. A specific range can be specified with [xWindowRange] and [yWindowRange].
 *
 * @receiver The current collection of points within which to establish the offsets.
 * @param size The size of the canvas to draw on.
 * @param xWindowRange (optional) The x-range from the chart visualization window.
 * @param yWindowRange (optional) The y-range from the chart visualization window.
 */
public fun Dataset.createOffsets(
    size: Size,
    xWindowRange: ClosedFloatingPointRange<Float> = xRange,
    yWindowRange: ClosedFloatingPointRange<Float> = yRange
) : Dataset {
    val canvasYRange = size.height..0f //Inverting to draw from bottom to top
    val canvasXRange = 0f..size.width
    return map {
        it.copy(
            offset = Offset(
                x = it.x.map(xWindowRange, canvasXRange),
                y = it.y.map(yWindowRange, canvasYRange),
            )
        )
    }
}

/**
 * Returns a position inside the given data range, relative to a given X position in the canvas with
 * the same logic from [map].
 *
 * @param canvasPos X position in the current canvas.
 * @param dataRange Range of the dataset
 */
public fun DrawScope.mapCanvasXToDataX(
    canvasPos: Float,
    dataRange: ClosedFloatingPointRange<Float>
): Float = canvasPos.map(0f..size.width, dataRange)

/**
 * Returns a position inside the given data range, relative to a given Y position in the canvas with
 * the same logic from [map].
 *
 * @param canvasPos Y position in the current canvas.
 * @param dataRange Range of the dataset
 */
public fun DrawScope.mapCanvasYToDataY(
    canvasPos: Float,
    dataRange: ClosedFloatingPointRange<Float>
): Float = canvasPos.map(0f..size.height, dataRange)
