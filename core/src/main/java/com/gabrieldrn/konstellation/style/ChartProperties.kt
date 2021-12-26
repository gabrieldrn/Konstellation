package com.gabrieldrn.konstellation.style

import com.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of properties that can be applied to a chart.
 * @property axes Axes to be drawn on the chart.
 * @property lineStyle Appearance of the lines connecting points.
 * @property pointStyle Appearance of data points.
 */
interface ChartProperties {
    val axes: Set<ChartAxis>
    val lineStyle: LineDrawStyle
    val pointStyle: PointDrawStyle
}
