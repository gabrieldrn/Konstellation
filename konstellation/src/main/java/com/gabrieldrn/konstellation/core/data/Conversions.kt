package com.gabrieldrn.konstellation.core.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.gabrieldrn.konstellation.core.plotting.Dataset
import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.core.plotting.xRange
import com.gabrieldrn.konstellation.core.plotting.yRange
import kotlin.math.abs

/**
 * Converts a value from a range to another value based on another range, so as they are equivalent
 * between the two ranges.
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
internal fun Float.convertFromRanges(
    fromRange: ClosedRange<Float>,
    toRange: ClosedRange<Float>
): Float {
//    require(this in fromRange) {
//        "The point must be within the bounds of his range"
//    }
    val offsetRange = if (toRange.start < 0.0) abs(toRange.start) else 0f
    return (((toRange.endInclusive + offsetRange) - (toRange.start + offsetRange))
            * (this - fromRange.start) / (fromRange.endInclusive - fromRange.start)
            ) - offsetRange
}

/**
 * Sets all the offsets in each [Point] of the receiving collection, based on the bounds of the
 * passed [drawScope]. A specific Y range can be given with [dataSetYRange].
 *
 * @receiver The current collection of point within which to establish the offsets.
 * @param drawScope The draw scope of the chart in which drawing the future offsets.
 * @param dataSetYRange (optional) A specific range of values on the Y axis to consider instead of
 * the Y range of the receiving collection.
 */
internal fun Dataset.createOffsets(
    drawScope: DrawScope,
    dataSetYRange: ClosedRange<Float> = this.yRange
) {
    val canvasYRange = drawScope.size.height..0f //Inverting to draw from bottom to top
    val canvasXRange = 0f..drawScope.size.width
    forEach {
        it.offset = Offset(
            x = it.x.convertFromRanges(xRange, canvasXRange),
            y = it.y.convertFromRanges(dataSetYRange, canvasYRange) + drawScope.size.height,
        )
    }
}

internal fun Point.createOffset(
    drawScope: DrawScope,
    datasetXRange: ClosedRange<Float>,
    datasetYRange: ClosedRange<Float>
) {
    val canvasYRange = drawScope.size.height..0f //Inverting to draw from bottom to top
    val canvasXRange = 0f..drawScope.size.width
    offset = Offset(
        x = x.convertFromRanges(datasetXRange, canvasXRange),
        y = y.convertFromRanges(datasetYRange, canvasYRange) + drawScope.size.height,
    )
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
    range: ClosedRange<Float>
) = canvasPos.toFloat().convertFromRanges(0f..size.width, range)
