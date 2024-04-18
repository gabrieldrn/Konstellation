package dev.gabrieldrn.konstellation.plotting

import android.graphics.Paint
import androidx.compose.runtime.*
import dev.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle

/**
 * Represents a chart axis.
 * @property axis Position of the axis within the chart.
 * @property tickCount Maximum number of ticks that will be drawn on this axis.
 */
@Immutable
public data class ChartAxis(val axis: Axis, val tickCount: Int)

/**
 * Positions of an axis.
 */
public enum class Axis {
    X_BOTTOM,
    X_TOP,
    Y_LEFT,
    Y_RIGHT;

    public companion object {

        /**
         * Returns true if this axis is horizontal.
         */
        public val Axis.isHorizontal: Boolean
            get() = this == X_BOTTOM || this == X_TOP

        /**
         * Returns true if this axis is vertical.
         */
        public val Axis.isVertical: Boolean
            get() = this == Y_LEFT || this == Y_RIGHT
    }
}

/**
 * Convenience singleton, providing all default axes for a chart.
 */
@Immutable
public object Axes {

    /**
     * Default bottom X-axis.
     */
    public val xBottom: ChartAxis = ChartAxis(
        axis = Axis.X_BOTTOM,
        tickCount = 5
    )

    /**
     * Default bottom X-axis style.
     */
    public val xBottomStyle: AxisDrawStyle = AxisDrawStyle(
        tickLabelStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )

    /**
     * Default top axis.
     */
    public val xTop: ChartAxis = ChartAxis(
        axis = Axis.X_TOP,
        tickCount = 5
    )

    /**
     * Default top X-axis style.
     */
    public val xTopStyle: AxisDrawStyle = AxisDrawStyle(
        tickLabelStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )

    /**
     * Default left Y-axis.
     */
    public val yLeft: ChartAxis = ChartAxis(
        axis = Axis.Y_LEFT,
        tickCount = 5
    )

    /**
     * Default left Y-axis style.
     */
    public val yLeftStyle: AxisDrawStyle = AxisDrawStyle(
        tickLabelStyle = TextDrawStyle(
            textAlign = Paint.Align.RIGHT
        )
    )

    /**
     * Default right Y-axis.
     */
    public val yRight: ChartAxis = ChartAxis(
        axis = Axis.Y_RIGHT,
        tickCount = 5
    )

    /**
     * Default right Y-axis style.
     */
    public val yRightStyle: AxisDrawStyle = AxisDrawStyle(
        tickLabelStyle = TextDrawStyle(
            textAlign = Paint.Align.LEFT
        )
    )

    /**
     * Set of all default axes.
     */
    public val all: Set<ChartAxis> = setOf(xBottom, xTop, yLeft, yRight)
}
