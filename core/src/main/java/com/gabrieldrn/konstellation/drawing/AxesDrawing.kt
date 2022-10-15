package com.gabrieldrn.konstellation.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.math.*
import com.gabrieldrn.konstellation.math.calculateAxisOffsets
import com.gabrieldrn.konstellation.math.getAxisDrawingPoints
import com.gabrieldrn.konstellation.plotting.Axis
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.configuration.properties.ChartProperties
import com.gabrieldrn.konstellation.plotting.NiceScale
import com.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.ChartStyles
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.getAxisStyleByType
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
fun DrawScope.drawScaledAxis(
    properties: ChartProperties,
    styles: ChartStyles,
    xRange: ClosedFloatingPointRange<Float>,
    yRange: ClosedFloatingPointRange<Float>,
) {
    var range: ClosedFloatingPointRange<Float>
    var style: AxisDrawStyle
    properties.axes.forEach { axis ->
        range = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> xRange
            Axis.Y_LEFT, Axis.Y_RIGHT -> yRange
        }
        style = styles.getAxisStyleByType(axis)

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
        drawLine(lineStart, lineEnd, style.axisLineStyle)

        //Compute starting offsets for drawing
        val (tickSpacingOffset, lineStartOffset) = calculateAxisOffsets(
            axis, chartScale, range, lineStart
        )

        lineStart = lineStartOffset
        //First tick label value
        var tickValue = chartScale.niceMin
        //Labels drawing
        while (tickValue <= chartScale.niceMax) {
            if (tickValue in range) drawTick(lineStart, axis, tickValue.toString(), style)
            lineStart += tickSpacingOffset
            tickValue += chartScale.tickSpacing
        }
    }
}

private const val DefaultTickSize = 20f //TODO Move into style data class
private const val DefaultLabelXOffset = 20f //TODO Move into style data class

/**
 * Draws a tiny vertical line representing a tick, with a given [label]. The orientation of the tick
 * depends on the type of axis (X or Y).
 */
internal fun DrawScope.drawTick(
    position: Offset,
    axis: ChartAxis,
    label: String,
    style: AxisDrawStyle
) {
    drawLine(
        start = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(position.x, position.y - DefaultTickSize / 2)
            else -> Offset(position.x - DefaultTickSize / 2, position.y)
        },
        end = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(position.x, position.y + DefaultTickSize / 2)
            else -> Offset(position.x + DefaultTickSize / 2, position.y)
        },
        lineStyle = style.tickLineStyle.copy(cap = StrokeCap.Square)
    )
    drawIntoCanvas {
        tickLabelPaint.apply {
            textAlign = style.tickTextStyle.textAlign
            textSize = style.tickTextStyle.textSize.value
            color = style.tickTextStyle.color.toInt()
            typeface = style.tickTextStyle.typeface
            flags = Paint.ANTI_ALIAS_FLAG
        }
        val xMetricsOffset = when (axis.axis) {
            Axis.Y_LEFT -> -DefaultLabelXOffset
            Axis.Y_RIGHT -> DefaultLabelXOffset
            else -> 0f
        }
        val yMetricsOffset = when (axis.axis) {
            Axis.X_BOTTOM -> tickLabelPaint.fontMetrics.descent - tickLabelPaint.fontMetrics.ascent
            Axis.X_TOP -> -(tickLabelPaint.fontMetrics.bottom + tickLabelPaint.fontMetrics.descent)
            else -> -((tickLabelPaint.fontMetrics.ascent + tickLabelPaint.fontMetrics.descent) / 2)
        }
        it.nativeCanvas.drawText(
            label,
            (position.x + style.tickTextStyle.offsetX) + xMetricsOffset,
            (position.y + style.tickTextStyle.offsetY) + yMetricsOffset,
            tickLabelPaint
        )
    }
}

/**
 * Draws lines alongside canvas bounds.
 */
fun DrawScope.drawFrame(
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
fun DrawScope.drawZeroLines(
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
        zero = 0f.map(datasetYRange, size.height..0f) + size.height
        drawLine(Offset(0f, zero), Offset(size.width, zero), lineStyle)
    }
    if (verticalLine && 0f in datasetXRange) {
        zero = 0f.map(datasetXRange, 0f..size.width)
        drawLine(Offset(zero, 0f), Offset(zero, size.height), lineStyle)
    }
}
