package com.gabrieldrn.konstellation.linechart

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertFromRanges
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.toInt

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
    end = end
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

internal fun DrawScope.drawPoints(dataset: Dataset, style: PointDrawStyle) {
    dataset.forEach { drawPoint(it, style) }
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

internal fun DrawScope.drawMinMaxAxisValues(
    dataset: Dataset, textStyle: TextDrawStyle
) = with(dataset) {
    drawMinMaxAxisValues(xMin, xMax, yMin, yMax, textStyle)
}

/**
 * Highlights a given [point] in the chart by drawing another circle styled with
 * [highlightPointStyle] in front of it and a text showing its value on top, styled with
 * [highlightTextStyle].
 */
internal fun DrawScope.highlightPoint(
    point: Point,
    highlightPointStyle: PointDrawStyle,
    highlightTextStyle: TextDrawStyle
) {
    drawPoint(point, highlightPointStyle)
    drawLine(
            color = highlightPointStyle.color,
            strokeWidth = highlightPointStyle.radius.toPx() / 2,
            cap = StrokeCap.Square,
            start = Offset(point.offset.x, 0f),
            end = Offset(point.offset.x, size.height),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f))
    )
}

internal fun DrawScope.drawLabelPoints(
    dataset: Dataset, textStyle: TextDrawStyle,
    onDrawPointLabel: (point: Point) -> String = { "${it.y}" }
) {
    dataset.forEach {
        drawText(
            it.offset,
            text = onDrawPointLabel(it),
            style = textStyle
        )
    }
}

/**
 * Draws a text into the current DrawScope.
 */
internal fun DrawScope.drawText(
    point: Offset,
    text: String,
    style: TextDrawStyle = TextDrawStyle()
) = drawIntoCanvas {
    it.nativeCanvas.drawText(
        text,
        point.x + style.offsetX,
        point.y + style.offsetY,
        Paint().apply {
            textAlign = style.textAlign
            textSize = style.textSize
            color = style.color.toInt()
            typeface = style.typeface
            flags = Paint.ANTI_ALIAS_FLAG
        }
    )
}
