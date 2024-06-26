package dev.gabrieldrn.konstellation.configuration.styles

import dev.gabrieldrn.konstellation.plotting.Axis
import dev.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of styles that can be applied to components of a chart.
 */
public interface ChartStyles {

    /**
     * Axes to be drawn on the chart.
     */
    public val axes: Set<ChartAxis>

    /**
     * Either to draw the lines delimiting the chart or not.
     */
    public val drawFrame: Boolean

    /**
     * Either to draw the lines indicating the zero on X and Y axes or not.
     */
    public val drawZeroLines: Boolean

    /**
     * Appearance of the lines connecting points.
     */
    public val lineStyle: LineDrawStyle

    /**
     * Appearance of data points.
     */
    public val pointStyle: PointDrawStyle

    /**
     * Appearance of the bottom axis.
     */
    public val xAxisBottomStyle: AxisDrawStyle

    /**
     * Appearance of the top axis.
     */
    public val xAxisTopStyle: AxisDrawStyle

    /**
     * Appearance of the left axis.
     */
    public val yAxisLeftStyle: AxisDrawStyle

    /**
     * Appearance of the right axis.
     */
    public val yAxisRightStyle: AxisDrawStyle
}

/**
 * Returns the corresponding style of the given [axis].
 */
public fun ChartStyles.getAxisStyleByType(axis: ChartAxis): AxisDrawStyle = when(axis.axis) {
    Axis.X_BOTTOM -> xAxisBottomStyle
    Axis.X_TOP -> xAxisTopStyle
    Axis.Y_LEFT -> yAxisLeftStyle
    Axis.Y_RIGHT -> yAxisRightStyle
}
