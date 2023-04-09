package com.gabrieldrn.konstellation.configuration.styles

import com.gabrieldrn.konstellation.plotting.Axis
import com.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of styles that can be applied to components of a chart.
 */
interface ChartStyles {

    /**
     * Appearance of the lines connecting points.
     */
    val lineStyle: LineDrawStyle

    /**
     * Appearance of data points.
     */
    val pointStyle: PointDrawStyle

    /**
     * Appearance of the bottom axis.
     */
    val xAxisBottomStyle: AxisDrawStyle

    /**
     * Appearance of the top axis.
     */
    val xAxisTopStyle: AxisDrawStyle

    /**
     * Appearance of the left axis.
     */
    val yAxisLeftStyle: AxisDrawStyle

    /**
     * Appearance of the right axis.
     */
    val yAxisRightStyle: AxisDrawStyle
}

/**
 * Returns the corresponding style of the given [axis].
 */
fun ChartStyles.getAxisStyleByType(axis: ChartAxis) = when(axis.axis) {
    Axis.X_BOTTOM -> xAxisBottomStyle
    Axis.X_TOP -> xAxisTopStyle
    Axis.Y_LEFT -> yAxisLeftStyle
    Axis.Y_RIGHT -> yAxisRightStyle
}
