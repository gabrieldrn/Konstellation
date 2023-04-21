package com.gabrieldrn.konstellation.drawing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

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
