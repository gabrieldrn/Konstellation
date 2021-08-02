package com.gabrieldrn.konstellation.core.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.core.plotting.ChartProperties
import com.gabrieldrn.konstellation.linechart.drawLine
import com.gabrieldrn.konstellation.core.plotting.NiceScale
import com.gabrieldrn.konstellation.util.rawRange
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
 * Draws axis and labels of a chart based on a [dataSet] and with given [properties].
 */
internal fun DrawScope.drawScaledAxis(
    properties: ChartProperties,
    dataSet: Dataset
) {
    var range: ClosedFloatingPointRange<Float>
    var lineStart: Offset
    var lineEnd: Offset

    properties.axes.forEach { axis ->
        range = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> properties.dataXRange ?: dataSet.xRange
            Axis.Y_LEFT, Axis.Y_RIGHT -> properties.dataYRange ?: dataSet.yRange
        }

        //Axis scale computation
        chartScale.run {
            dataRange = range
            maxTicks = axis.tickCount
            compute()
        }

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

        val absoluteLength = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> size.width
            else -> size.height
        }
        //Space between left canvas border and left chart "window" depending on chart values
        val startSpace = (absoluteLength * (range.start - chartScale.niceMin))/ range.rawRange
        //Space between right chart "window" and right canvas border depending on chart values
        val endSpace = (absoluteLength * (chartScale.niceMax - range.endInclusive)) / range.rawRange
        //Number of ticks
        val tickCount = (chartScale.niceMax - chartScale.niceMin) / chartScale.tickSpacing + 1
        //Space between each tick
        val tickSpacing = (absoluteLength + startSpace + endSpace) / (tickCount - 1)
        val tickSpacingOffset = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(tickSpacing, 0f)
            else -> Offset(0f, -tickSpacing)
        }
        //Potential shift of the initial drawing point at the beginning of the axis if offset != 0
        lineStart += when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> -Offset(startSpace, 0f)
            else -> Offset(0f, startSpace)
        }
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
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(position.x, position.y - 10f)
            else -> Offset(position.x - 10f, position.y)
        },
        end = when (axis.axis) {
            Axis.X_TOP, Axis.X_BOTTOM -> Offset(position.x, position.y + 10f)
            else -> Offset(position.x + 10f, position.y)
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
            Axis.Y_LEFT -> -20f
            Axis.Y_RIGHT -> 20f
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
