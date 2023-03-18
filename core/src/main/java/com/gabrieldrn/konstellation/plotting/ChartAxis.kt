package com.gabrieldrn.konstellation.plotting

import android.graphics.Paint
import com.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle

/**
 * Represents a chart axis.
 * @param axis Position of the axis within the chart
 * @param tickCount Maximum number of ticks that will be drawn on this axis.
 */
data class ChartAxis(var axis: Axis, var tickCount: Int)

/**
 * Positions of an axis.
 */
enum class Axis {
    X_BOTTOM,
    X_TOP,
    Y_LEFT,
    Y_RIGHT,
}

/**
 * Convenience singleton, providing all default axes for a chart.
 */
object Axes {

    val xBottom = ChartAxis(
        axis = Axis.X_BOTTOM,
        tickCount = 5
    )

    val xBottomStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )

    val xTop = ChartAxis(
        axis = Axis.X_TOP,
        tickCount = 5
    )

    val xTopStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )

    val yLeft = ChartAxis(
        axis = Axis.Y_LEFT,
        tickCount = 5
    )

    val yLeftStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.RIGHT
        )
    )

    val yRight = ChartAxis(
        axis = Axis.Y_RIGHT,
        tickCount = 5
    )

    val yRightStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.LEFT
        )
    )

    val all = setOf(xBottom, xTop, yLeft, yRight)
}
