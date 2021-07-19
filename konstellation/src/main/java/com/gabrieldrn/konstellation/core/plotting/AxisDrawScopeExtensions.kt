package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.gabrieldrn.konstellation.util.NiceScale
import com.gabrieldrn.konstellation.util.rawRange
import com.gabrieldrn.konstellation.util.toInt

internal fun DrawScope.drawScaledAxis(
    properties: ChartProperties,
    dataSet: Dataset
) {
    var range: ClosedFloatingPointRange<Float>
    var scaleCalc: NiceScale
    var lineStart: Offset
    var lineEnd: Offset

    properties.axes.forEach { axis ->
        range = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> properties.dataXRange ?: dataSet.xRange
            Axis.Y_LEFT, Axis.Y_RIGHT -> properties.dataYRange ?: dataSet.yRange
        }

        //Axis scale computation
        scaleCalc = NiceScale(range, axis.tickCount)

        //Axis line offsets
        //lineStart = initial drawing point
        when (axis.axis) {
            Axis.X_TOP -> Offset(0f, 0f) to Offset(size.width, 0f)
            Axis.X_BOTTOM -> Offset(0f, size.height) to Offset(size.width, size.height)
            Axis.Y_LEFT -> Offset(0f, size.height) to Offset(0f, 0f)
            Axis.Y_RIGHT -> Offset(size.width, size.height) to Offset(size.width, 0f)
        }.run { lineStart = first; lineEnd = second }

        //Axis line
        drawLine(lineStart, lineEnd, axis.style.axisLineStyle)

        val startEndOffsetSpace = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> size.width
            else -> size.height
        }
        //Space between left canvas border and left chart "window" depending on chart values
        val startOffset = (startEndOffsetSpace * (range.start - scaleCalc.niceMin))/ range.rawRange
        //Space between right chart "window" and right canvas border depending on chart values
        val endOffset = (startEndOffsetSpace * (scaleCalc.niceMax - range.endInclusive)) / range.rawRange
        //Number of ticks
        val tickCount = (scaleCalc.niceMax - scaleCalc.niceMin) / scaleCalc.tickSpacing + 1
        //Space between each tick
        val tickSpacing = (startEndOffsetSpace + startOffset + endOffset) / (tickCount - 1)
        val tickSpacingOffset = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(tickSpacing, 0f)
            else -> Offset(0f, -tickSpacing)
        }
        //Potential shift of the initial drawing point on the left if offset != 0
        lineStart -= Offset(startOffset, 0f)
        //First tick label value
        var tickValue = scaleCalc.niceMin

        //Labels drawing
        while (tickValue <= scaleCalc.niceMax) {
            if (tickValue in range) {
                drawTick(lineStart, axis)
                drawTickLabel(Offset(lineStart.x, lineStart.y), axis, tickValue.toString())
            }
            lineStart += tickSpacingOffset
            tickValue += scaleCalc.tickSpacing
        }
    }
}

/**
 * Draws a tiny vertical line representing a tick.
 */
internal fun DrawScope.drawTick(
    center: Offset,
    axis: ChartAxis
) {
    drawLine(
        start = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(center.x, center.y - 10f)
            else -> Offset(center.x - 10f, center.y)
        },
        end = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(center.x, center.y + 10f)
            else -> Offset(center.x + 10f, center.y)
        },
        lineStyle = axis.style.tickLineStyle.copy(cap = StrokeCap.Square)
    )
}

/**
 * Draws a text into the current DrawScope.
 */
internal fun DrawScope.drawTickLabel(
    point: Offset,
    axis: ChartAxis,
    text: String
) = drawIntoCanvas {
    val paint = Paint().apply {
        textAlign = axis.style.tickTextStyle.textAlign
        textSize = axis.style.tickTextStyle.textSize
        color = axis.style.tickTextStyle.color.toInt()
        typeface = axis.style.tickTextStyle.typeface
        flags = Paint.ANTI_ALIAS_FLAG
    }
    val xMetricsOffset = when (axis.axis) {
        Axis.Y_LEFT -> -20f
        Axis.Y_RIGHT -> 20f
        else -> 0f
    }
    val yMetricsOffset = when (axis.axis) {
        Axis.X_BOTTOM -> paint.fontMetrics.descent - paint.fontMetrics.ascent
        Axis.X_TOP -> -(paint.fontMetrics.bottom + paint.fontMetrics.descent)
        else -> -((paint.fontMetrics.ascent + paint.fontMetrics.descent) / 2)
    }
    it.nativeCanvas.drawText(
        text,
        (point.x + axis.style.tickTextStyle.offsetX) + xMetricsOffset,
        (point.y + axis.style.tickTextStyle.offsetY) + yMetricsOffset,
        paint
    )
}
