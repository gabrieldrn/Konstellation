package com.gabrieldrn.konstellation.charts.line

import android.graphics.Paint
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.core.drawing.drawText
import com.gabrieldrn.konstellation.core.geometry.convertFromRanges
import com.gabrieldrn.konstellation.core.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.core.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.core.highlighting.verticalHLPositions
import com.gabrieldrn.konstellation.core.plotting.Dataset
import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

/**
 * Draws a line between the given offsets using the given style. The line is stroked.
 */
internal fun DrawScope.drawLine(
    start: Offset, end: Offset,
    lineStyle: LineDrawStyle = LineDrawStyle()
) = drawLine(
    color = lineStyle.color,
    strokeWidth = lineStyle.strokeWidth.toPx(),
    cap = lineStyle.cap,
    start = start,
    end = end,
    pathEffect = if (lineStyle.dashed)
        PathEffect.dashPathEffect(lineStyle.dashedEffectIntervals, lineStyle.dashedEffectPhase)
    else null
)

/**
 * Draws a line between the given points using the given style. The line is stroked.
 */
internal fun DrawScope.drawLine(
    start: Point, end: Point,
    lineStyle: LineDrawStyle = LineDrawStyle()
) = drawLine(start.offset, end.offset, lineStyle)

/**
 * Draws a series of stroked lines from a dataset, using the given style.
 */
internal fun DrawScope.drawLines(
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
 * Draws a circle representing a point of the dataset
 */
internal fun DrawScope.drawPoint(point: Point, style: PointDrawStyle = PointDrawStyle()) {
    drawCircle(center = point.offset, color = style.color, radius = style.radius.toPx())
}

/**
 * Draws lines along canvas bounds.
 */
internal fun DrawScope.drawFrame(
    lineStyle: LineDrawStyle = LineDrawStyle(
        color = Color.LightGray,
        strokeWidth = 1.5f.dp,
        cap = StrokeCap.Square,
    )
) {
    drawLine(Offset(0f, 0f), Offset(size.width, 0f), lineStyle)
    drawLine(Offset(size.width, 0f), Offset(size.width, size.height), lineStyle)
    drawLine(Offset(0f, 0f), Offset(0f, size.height), lineStyle)
    drawLine(Offset(0f, size.height), Offset(size.width, size.height), lineStyle)
}

/**
 * Draws the 0-axis inside the chart.
 */
internal fun DrawScope.drawZeroLines(
    datasetXRange: ClosedRange<Float>,
    datasetYRange: ClosedRange<Float>,
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
        zero = 0f.convertFromRanges(datasetYRange, size.height..0f) + size.height
        drawLine(Offset(0f, zero), Offset(size.width, zero), lineStyle)
    }
    if (verticalLine && 0f in datasetXRange) {
        zero = 0f.convertFromRanges(datasetXRange, 0f..size.width)
        drawLine(Offset(zero, 0f), Offset(zero, size.height), lineStyle)
    }
}

internal fun DrawScope.drawMinMaxAxisValues(
    xMin: Number, xMax: Number,
    yMin: Number, yMax: Number,
    textStyle: TextDrawStyle
) {
    drawText(
        Offset(0f, size.height),
        text = xMin.toString(),
        style = textStyle.copy(offsetY = 25f)
    )
    drawText(
        Offset(size.width, size.height),
        text = xMax.toString(),
        style = textStyle.copy(textAlign = Paint.Align.RIGHT, offsetY = 25f)
    )
    drawText(
        Offset(0f, size.height),
        text = yMin.toString(),
        style = textStyle
    )
    drawText(
        Offset(0f, 0f),
        text = yMax.toString(),
        style = textStyle.copy(offsetY = 25f)
    )
}

/**
 * Highlights a given [point] in the chart by drawing another circle styled with
 * [highlightPointStyle] in front of it and dashed lines based on [highlightPositions].
 */
internal fun DrawScope.highlightPoint(
    point: Point,
    highlightPositions: Array<HighlightPosition>,
    highlightPointStyle: PointDrawStyle,
    highlightLineStyle: LineDrawStyle
) {
    drawPoint(point, highlightPointStyle)
    if (highlightPositions.any { it in verticalHLPositions }) {
        drawLine(
            Offset(point.offset.x, 0f),
            Offset(point.offset.x, size.height),
            highlightLineStyle
        )
    }
    if (highlightPositions.any { it in horizontalHLPositions }) {
        drawLine(
            Offset(0f, point.yPos),
            Offset(size.width, point.yPos),
            highlightLineStyle
        )
    }
}
