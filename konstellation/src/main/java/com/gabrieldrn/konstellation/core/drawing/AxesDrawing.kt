package com.gabrieldrn.konstellation.core.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.charts.line.drawLine
import com.gabrieldrn.konstellation.core.geometry.calculateAxisOffsets
import com.gabrieldrn.konstellation.core.geometry.getAxisDrawingPoints
import com.gabrieldrn.konstellation.core.plotting.Axis
import com.gabrieldrn.konstellation.core.plotting.ChartAxis
import com.gabrieldrn.konstellation.core.plotting.ChartProperties
import com.gabrieldrn.konstellation.core.plotting.NiceScale
import com.gabrieldrn.konstellation.util.toInt

/**
 * Allocate a NiceScale object to compute the ticks of this chart.
 */
private val chartScale = NiceScale(0f..1f)

/**
 * Allocate a Paint object for the label of a tick.
 */
private val tickLabelPaint = Paint()

/**
 * Draws axis and labels of a chart based on a given [properties] and axis ranges [xRange],
 * [yRange].
 */
internal fun DrawScope.drawScaledAxis(
    properties: ChartProperties,
    xRange: ClosedFloatingPointRange<Float>,
    yRange: ClosedFloatingPointRange<Float>,
) {
    var range: ClosedFloatingPointRange<Float>

    properties.axes.forEach { axis ->
        range = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> xRange
            Axis.Y_LEFT, Axis.Y_RIGHT -> yRange
        }

        //Axis scale computation
        chartScale.run {
            dataRange = range
            maxTicks = axis.tickCount
            compute()
        }

        //Axis line offsets
        //lineStart = initial drawing point
        var (lineStart, lineEnd) = getAxisDrawingPoints(axis)

        //Axis line
        drawLine(lineStart, lineEnd, axis.style.axisLineStyle)

        //Compute starting offsets for drawing
        val (tickSpacingOffset, lineStartOffset) = calculateAxisOffsets(
            axis, chartScale, range, lineStart
        )

        lineStart = lineStartOffset
        //First tick label value
        var tickValue = chartScale.niceMin
        //Labels drawing
        while (tickValue <= chartScale.niceMax) {
            if (tickValue in range) drawTick(lineStart, axis, tickValue.toString())
            lineStart += tickSpacingOffset
            tickValue += chartScale.tickSpacing
        }
    }
}

private const val DEFAULT_TICK_SIZE = 20f //TODO Move into style data class
private const val DEFAULT_LABEL_X_OFFSET = 20f //TODO Move into style data class

/**
 * Draws a tiny vertical line representing a tick, with a given [label]. The orientation of the tick
 * depends on the type of axis (X or Y).
 */
internal fun DrawScope.drawTick(
    position: Offset,
    axis: ChartAxis,
    label: String
) {
    drawLine(
        start = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(position.x, position.y - DEFAULT_TICK_SIZE / 2)
            else -> Offset(position.x - DEFAULT_TICK_SIZE / 2, position.y)
        },
        end = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(position.x, position.y + DEFAULT_TICK_SIZE / 2)
            else -> Offset(position.x + DEFAULT_TICK_SIZE / 2, position.y)
        },
        lineStyle = axis.style.tickLineStyle.copy(cap = StrokeCap.Square)
    )
    drawIntoCanvas {
        tickLabelPaint.apply {
            textAlign = axis.style.tickTextStyle.textAlign
            textSize = axis.style.tickTextStyle.textSize
            color = axis.style.tickTextStyle.color.toInt()
            typeface = axis.style.tickTextStyle.typeface
            flags = Paint.ANTI_ALIAS_FLAG
        }
        val xMetricsOffset = when (axis.axis) {
            Axis.Y_LEFT -> -DEFAULT_LABEL_X_OFFSET
            Axis.Y_RIGHT -> DEFAULT_LABEL_X_OFFSET
            else -> 0f
        }
        val yMetricsOffset = when (axis.axis) {
            Axis.X_BOTTOM -> tickLabelPaint.fontMetrics.descent - tickLabelPaint.fontMetrics.ascent
            Axis.X_TOP -> -(tickLabelPaint.fontMetrics.bottom + tickLabelPaint.fontMetrics.descent)
            else -> -((tickLabelPaint.fontMetrics.ascent + tickLabelPaint.fontMetrics.descent) / 2)
        }
        it.nativeCanvas.drawText(
            label,
            (position.x + axis.style.tickTextStyle.offsetX) + xMetricsOffset,
            (position.y + axis.style.tickTextStyle.offsetY) + yMetricsOffset,
            tickLabelPaint
        )
    }
}
