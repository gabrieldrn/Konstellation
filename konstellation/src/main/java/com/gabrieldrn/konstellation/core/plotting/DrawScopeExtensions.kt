package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertFromRanges
import com.gabrieldrn.konstellation.core.data.createOffset
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.toInt

/**
 * Draws a line between the given points using the given style. The line is stroked.
 */
internal fun DrawScope.drawLine(
    start: Offset,
    end: Offset,
    lineStyle: LineDrawStyle = LineDrawStyle()
) {
    drawLine(
        color = lineStyle.color,
        strokeWidth = lineStyle.strokeWidth.toPx(),
        cap = lineStyle.cap,
        start = start,
        end = end
    )
}

/**
 * Draws a series of stroked lines from a dataset, using the given style.
 */
internal fun DrawScope.drawLines(dataset: Dataset, lineStyle: LineDrawStyle) {
    if (dataset.isEmpty()) return
    var previous = dataset.first().offset
    val d = dataset.iterator()
    while (d.hasNext()) {
        d.next().let {
            drawLine(previous, it.offset, lineStyle)
            previous = it.offset
        }
    }
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
    drawLine(Offset(size.width, 0f),Offset(size.width, size.height), lineStyle)
    drawLine(Offset(0f, 0f),Offset(0f, size.height), lineStyle)
    drawLine(Offset(0f, size.height),Offset(size.width, size.height), lineStyle)
}

/**
 * Draws a horizontal line at the middle of the canvas.
 */
internal fun DrawScope.drawMiddleHorizontalLine(color: Color = Color.Gray) =
    drawLine(color, Offset(0f, size.height / 2), Offset(size.width - 1, size.height / 2))

/**
 * Draws a vertical line at the middle of the canvas.
 */
internal fun DrawScope.drawMiddleVerticalLine(color: Color = Color.Gray) =
    drawLine(color, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height - 1))

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
    xMin: Number,
    xMax: Number,
    yMin: Number,
    yMax: Number,
    textStyle: TextDrawStyle
) {
    drawText(
        Offset(0f, size.height),
        offsetY = 25f,
        text = xMin.toString(),
        textAlign = Paint.Align.LEFT,
        typeface = textStyle.typeface,
        color = textStyle.color.toInt()
    )
    drawText(
        Offset(size.width, size.height),
        offsetY = 25f,
        text = xMax.toString(),
        textAlign = Paint.Align.RIGHT,
        typeface = textStyle.typeface,
        color = textStyle.color.toInt()
    )
    drawText(
        Offset(0f, size.height),
        text = yMin.toString(),
        textAlign = Paint.Align.LEFT,
        typeface = textStyle.typeface,
        color = textStyle.color.toInt()
    )
    drawText(
        Offset(0f, 0f),
        offsetY = 25f,
        text = yMax.toString(),
        textAlign = Paint.Align.LEFT,
        typeface = textStyle.typeface,
        color = textStyle.color.toInt()
    )
}

internal fun DrawScope.drawMinMaxAxisValues(
    dataset: Dataset, textStyle: TextDrawStyle
) = with(dataset) {
    drawMinMaxAxisValues(xMin, xMax, yMin, yMax, textStyle)
}

internal fun DrawScope.drawLabelPoints(
    dataset: Dataset, textStyle: TextDrawStyle,
    onDrawPointLabel: (point: Point) -> String = { "${it.y}" }
) {
    dataset.forEach {
        drawText(
            it.offset,
            10f,
            -10f,
            text = onDrawPointLabel(it),
            textAlign = Paint.Align.LEFT,
            typeface = textStyle.typeface
        )
    }
}

internal fun DrawScope.drawChartTitle(
    chartName: String,
    textStyle: TextDrawStyle
) {
    drawText(
        Offset(size.width / 2, 0f),
        text = chartName,
        offsetY = -25f,
        textAlign = Paint.Align.CENTER,
        textSize = 50f,
        typeface = textStyle.typeface,
        color = textStyle.color.toInt()
    )
}

/**
 * Draws a text into the current DrawScope.
 */
internal fun DrawScope.drawText(
    point: Offset,
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    text: String,
    textAlign: Paint.Align = Paint.Align.CENTER,
    textSize: Float = 32f,
    typeface: Typeface,
    color: Int = android.graphics.Color.GRAY
) {
    drawIntoCanvas {
        it.nativeCanvas.drawText(
            text,
            point.x + offsetX,
            point.y + offsetY,
            Paint().apply {
                this.textAlign = textAlign
                this.textSize = textSize
                this.color = color
                this.typeface = typeface
                flags = Paint.ANTI_ALIAS_FLAG
            }
        )
    }
}
