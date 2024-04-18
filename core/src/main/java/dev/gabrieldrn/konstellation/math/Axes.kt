package dev.gabrieldrn.konstellation.math

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import dev.gabrieldrn.konstellation.plotting.Axis
import dev.gabrieldrn.konstellation.plotting.ChartAxis
import dev.gabrieldrn.konstellation.plotting.NiceScale
import dev.gabrieldrn.konstellation.util.distance

/**
 * Returns the raw positions between where this [axis] should be drawn. The first offset is the
 * start position and the second one is the end.
 */
internal fun DrawScope.getAxisDrawingPoints(
    axis: ChartAxis,
) = when (axis.axis) {
    Axis.X_TOP -> Offset(0f, 0f) to Offset(size.width, 0f)
    Axis.X_BOTTOM -> Offset(0f, size.height) to Offset(size.width, size.height)
    Axis.Y_LEFT -> Offset(0f, size.height) to Offset(0f, 0f)
    Axis.Y_RIGHT -> Offset(size.width, size.height) to Offset(size.width, 0f)
}

/**
 * Returns the positions indicating the space where ticks of this [axis] should be drawn. The first
 * offset is the start position and the second one is the end.
 */
internal fun DrawScope.calculateAxisOffsets(
    axis: ChartAxis,
    chartScale: NiceScale,
    range: ClosedFloatingPointRange<Float>,
    lineStart: Offset
): Pair<Offset, Offset> {
    val length = when (axis.axis) {
        Axis.X_TOP, Axis.X_BOTTOM -> size.width
        else -> size.height
    }
    //Space between left canvas border and left chart "window" depending on chart values
    val startSpace = length * (range.start - chartScale.niceMin) / range.distance
    //Space between right chart "window" and right canvas border depending on chart values
    val endSpace = length * (chartScale.niceMax - range.endInclusive) / range.distance
    //Number of ticks
    val tickCount = (chartScale.niceMax - chartScale.niceMin) / chartScale.tickSpacing + 1

    //Space between each tick
    val tickSpacing = (length + startSpace + endSpace) / (tickCount - 1)
    val tickSpacingOffset = when (axis.axis) {
        Axis.X_TOP, Axis.X_BOTTOM -> Offset(tickSpacing, 0f)
        else -> Offset(0f, -tickSpacing)
    }

    //Potential shift of the initial drawing point at the beginning of the axis if offset != 0
    val lineStartOffset = lineStart + when (axis.axis) {
        Axis.X_TOP, Axis.X_BOTTOM -> -Offset(startSpace, 0f)
        else -> Offset(0f, startSpace)
    }

    return tickSpacingOffset to lineStartOffset
}
