package com.gabrieldrn.konstellation.plotting

import android.graphics.Paint
import com.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle

/**
 * Represents a chart axis.
 * @property axis Position of the axis within the chart.
 * @property tickCount Maximum number of ticks that will be drawn on this axis.
 */
data class ChartAxis(val axis: Axis, val tickCount: Int)

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

    /**
     * Default bottom X-axis.
     */
    val xBottom = ChartAxis(
        axis = Axis.X_BOTTOM,
        tickCount = 5
    )

    /**
     * Default bottom X-axis style.
     */
    val xBottomStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )

    /**
     * Default top axis.
     */
    val xTop = ChartAxis(
        axis = Axis.X_TOP,
        tickCount = 5
    )

    /**
     * Default top X-axis style.
     */
    val xTopStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )

    /**
     * Default left Y-axis.
     */
    val yLeft = ChartAxis(
        axis = Axis.Y_LEFT,
        tickCount = 5
    )

    /**
     * Default left Y-axis style.
     */
    val yLeftStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.RIGHT
        )
    )

    /**
     * Default right Y-axis.
     */
    val yRight = ChartAxis(
        axis = Axis.Y_RIGHT,
        tickCount = 5
    )

    /**
     * Default right Y-axis style.
     */
    val yRightStyle = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.LEFT
        )
    )

    /**
     * Set of all default axes.
     */
    val all = setOf(xBottom, xTop, yLeft, yRight)
}
