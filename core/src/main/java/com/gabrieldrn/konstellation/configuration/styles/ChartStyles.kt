package com.gabrieldrn.konstellation.configuration.styles

import com.gabrieldrn.konstellation.plotting.Axis
import com.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of styles that can be applied to components of a chart.
 * @property lineStyle Appearance of the lines connecting points.
 * @property pointStyle Appearance of data points.
 * @property xAxisBottomStyle Appearance of the bottom axis.
 * @property xAxisTopStyle Appearance of the top axis.
 * @property yAxisLeftStyle Appearance of the left axis.
 * @property yAxisRightStyle Appearance of the right axis.
 */
interface ChartStyles {
    val lineStyle: LineDrawStyle
    val pointStyle: PointDrawStyle
    val xAxisBottomStyle: AxisDrawStyle
    val xAxisTopStyle: AxisDrawStyle
    val yAxisLeftStyle: AxisDrawStyle
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
