package com.gabrieldrn.konstellation.util

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.gabrieldrn.konstellation.core.plotting.Dataset
import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.core.plotting.xMax
import com.gabrieldrn.konstellation.core.plotting.xMin
import com.gabrieldrn.konstellation.core.plotting.yMax
import com.gabrieldrn.konstellation.core.plotting.yMin
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

internal fun DrawScope.drawLines(dataset: Dataset, lineStyle: LineDrawStyle) {
    if (dataset.isEmpty()) return
    var previous = dataset.first().offset
    val d = dataset.iterator()
    while (d.hasNext()) {
        d.next().let {
            drawLine(
                color = lineStyle.color,
                strokeWidth = lineStyle.strokeWidth,
                cap = StrokeCap.Round,
                start = previous,
                end = it.offset
            )
            previous = it.offset
        }
    }
}

/**
 * Draws lines along canvas bounds.
 */
internal fun DrawScope.drawFrame(color: Color = Color.Gray) {
    drawLine(color, Offset(0f, 0f), Offset(size.width, 0f))
    drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height))
    drawLine(color, Offset(0f, 0f), Offset(0f, size.height))
    drawLine(color, Offset(0f, size.height), Offset(size.width, size.height))
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

/**
 * Draws a text into the current DrawScope.
 */
fun DrawScope.drawText(
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
