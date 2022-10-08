package com.gabrieldrn.konstellation.configuration.properties

import com.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of properties that can be applied to a chart.
 * @property axes Axes to be drawn on the chart.
 */
interface ChartProperties {
    val axes: Set<ChartAxis>
    val rounding: Rounding
}

/**
 * How plotting shall render
 */
enum class Rounding {
    /**
     * Drawing between data points will be straight, for example, in a line chart, the lines between
     * data points will be straight lines.
     */
    None,

    /**
     * Drawing between each data points will use a simple cubic Bézier curve effect, without
     * adaptation from the positions of the other data points.
     */
    SimpleCubic
}
