package dev.gabrieldrn.konstellation.drawing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import dev.gabrieldrn.konstellation.math.map
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.Point

/**
 * Draws a line between the given offsets using the given style. The line is stroked.
 */
public fun DrawScope.drawLine(
    start: Offset, end: Offset,
    lineStyle: LineDrawStyle = LineDrawStyle()
) {
    drawLine(
        color = lineStyle.color,
        strokeWidth = lineStyle.strokeWidth.toPx(),
        cap = lineStyle.cap,
        start = start,
        end = end,
        pathEffect = if (lineStyle.dashed) {
            PathEffect.dashPathEffect(lineStyle.dashedEffectIntervals, lineStyle.dashedEffectPhase)
        } else null
    )
}

/**
 * Draws a line between the given points using the given style. The line is stroked.
 */
public fun DrawScope.drawLine(
    start: Point, end: Point,
    lineStyle: LineDrawStyle = LineDrawStyle()
) {
    drawLine(start.offset, end.offset, lineStyle)
}

/**
 * Draws a series of stroked lines from a [dataset], using the given [lineStyle]. If [drawPoints] is
 * true, data points will be drawn with the given [pointStyle].
 */
public fun DrawScope.drawLines(
    dataset: Dataset,
    lineStyle: LineDrawStyle,
    pointStyle: PointDrawStyle,
    drawPoints: Boolean = false,
) {
    if (dataset.isEmpty()) return
    var previous = dataset.first()
    var d = dataset.iterator()

    while (d.hasNext()) {
        d.next().let {
            drawLine(previous, it, lineStyle)
            previous = it
        }
    }
    d = dataset.iterator()
    if (drawPoints) {
        while (d.hasNext()) {
            drawPoint(d.next(), pointStyle)
        }
    }
}

/**
 * Draws the 0-axis inside the chart.
 * TODO Delete this. drawLimitLine in line chart is more relevant.
 */
public fun DrawScope.drawZeroLines(
    datasetXRange: ClosedFloatingPointRange<Float>,
    datasetYRange: ClosedFloatingPointRange<Float>,
    horizontalLine: Boolean = true,
    verticalLine: Boolean = true,
    lineStyle: LineDrawStyle = LineDrawStyle(
        color = Color.LightGray,
        strokeWidth = 1.5f.dp,
        cap = StrokeCap.Square,
    )
) {
    var zero: Float

    if (horizontalLine && 0f in datasetYRange) {
        zero = 0f map (datasetYRange to size.height..0f)
        drawLine(Offset(0f, zero), Offset(size.width, zero), lineStyle)
    }
    if (verticalLine && 0f in datasetXRange) {
        zero = 0f map (datasetXRange to 0f..size.width)
        drawLine(Offset(zero, 0f), Offset(zero, size.height), lineStyle)
    }
}
