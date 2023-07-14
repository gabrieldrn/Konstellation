package dev.gabrieldrn.konstellation.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.ChartStyles
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.getAxisStyleByType
import dev.gabrieldrn.konstellation.math.calculateAxisOffsets
import dev.gabrieldrn.konstellation.math.getAxisDrawingPoints
import dev.gabrieldrn.konstellation.plotting.Axis
import dev.gabrieldrn.konstellation.plotting.ChartAxis
import dev.gabrieldrn.konstellation.plotting.NiceScale
import dev.gabrieldrn.konstellation.util.toInt

/**
 * Allocate a NiceScale object to compute the ticks of this chart.
 */
private val chartScale = NiceScale(0f..1f)

/**
 * Allocate a Paint object for the label of a tick.
 */
private val tickLabelPaint = Paint()

/**
 * Draws axis and labels of a chart based on its [styles] and axis ranges [xRange], [yRange].
 * The [onDrawTick] callback is called for each tick of the axis, and it should return the label
 * (String) to be drawn.
 */
public fun DrawScope.drawScaledAxis(
    styles: ChartStyles,
    xRange: ClosedFloatingPointRange<Float>,
    yRange: ClosedFloatingPointRange<Float>,
    onDrawTick: (ChartAxis, Float) -> String = { _, t -> t.toString() }
) {
    var range: ClosedFloatingPointRange<Float>
    var style: AxisDrawStyle
    styles.axes.forEach { axis ->
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
        drawLine(
            start = lineStart,
            end = lineEnd,
            lineStyle = style.axisLineStyle
        )

        //Compute starting offsets for drawing
        val (tickSpacingOffset, lineStartOffset) = calculateAxisOffsets(
            axis = axis,
            chartScale = chartScale,
            range = range,
            lineStart = lineStart
        )

        lineStart = lineStartOffset
        //First tick label value
        var tickValue = chartScale.niceMin
        //Labels drawing
        while (tickValue <= chartScale.niceMax) {
            if (tickValue in range) {
                drawTick(
                    position = lineStart,
                    axis = axis,
                    label = onDrawTick(axis, tickValue),
                    style = style
                )
            }
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
@Suppress("CyclomaticComplexMethod")
internal fun DrawScope.drawTick(
    position: Offset,
    axis: ChartAxis,
    label: String,
    style: AxisDrawStyle
) {
    // Tick line
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
    // Tick label
    drawIntoCanvas {
        tickLabelPaint.apply {
            textAlign = style.tickTextStyle.textAlign
            setTextSize(style.tickTextStyle, this@drawTick)
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
            position.x + style.tickTextStyle.offsetX + xMetricsOffset,
            position.y + style.tickTextStyle.offsetY + yMetricsOffset,
            tickLabelPaint
        )
    }
}

/**
 * Draws lines alongside canvas bounds.
 */
public fun DrawScope.drawFrame(
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
