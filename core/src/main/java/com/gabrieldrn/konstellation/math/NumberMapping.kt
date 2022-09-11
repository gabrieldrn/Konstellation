package com.gabrieldrn.konstellation.math

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.xRange
import com.gabrieldrn.konstellation.plotting.yRange
import kotlin.math.abs

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
 * @receiver The initial value into, must be bounded into [fromRange].
 * @param fromRange Initial range of the receiver.
 * @param toRange Targeted range.
 * @return The value converted with the targeted range.
 */
fun Float.map(
    fromRange: ClosedRange<Float>,
    toRange: ClosedRange<Float>
): Float {
    val offsetRange = if (toRange.start < 0f) abs(toRange.start) else 0f
    return (((toRange.endInclusive + offsetRange) - (toRange.start + offsetRange))
            * (this - fromRange.start) / (fromRange.endInclusive - fromRange.start)
            ) - offsetRange
}

/**
 * Sets the offset attribute of each [Point] of the receiving collection, based on the bounds of the
 * passed [drawScope]. A specific range can be given with [dataSetXRange] and [dataSetYRange].
 *
 * @receiver The current collection of points within which to establish the offsets.
 * @param drawScope The draw scope of the chart in which drawing the future offsets.
 * @param dataSetXRange (optional) A specific range of values on the X-axis to consider instead of
 * the X range of the receiving collection.
 * @param dataSetYRange (optional) A specific range of values on the Y-axis to consider instead of
 * the Y range of the receiving collection.
 */
fun Dataset.createOffsets(
    drawScope: DrawScope,
    dataSetXRange: ClosedRange<Float> = this.xRange,
    dataSetYRange: ClosedRange<Float> = this.yRange
) {
    val canvasYRange = drawScope.size.height..0f //Inverting to draw from bottom to top
    val canvasXRange = 0f..drawScope.size.width
    forEach {
        it.offset = Offset(
            x = it.x.map(dataSetXRange, canvasXRange),
            y = it.y.map(dataSetYRange, canvasYRange) + drawScope.size.height,
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
fun DrawScope.mapCanvasXToDataX(
    canvasPos: Float,
    dataRange: ClosedRange<Float>
) = canvasPos.map(0f..size.width, dataRange)

/**
 * Returns a position inside the given data range, relative to a given Y position in the canvas with
 * the same logic from [map].
 *
 * @param canvasPos Y position in the current canvas.
 * @param dataRange Range of the dataset
 */
fun DrawScope.mapCanvasYToDataY(
    canvasPos: Float,
    dataRange: ClosedRange<Float>
) = canvasPos.map(0f..size.height, dataRange)
