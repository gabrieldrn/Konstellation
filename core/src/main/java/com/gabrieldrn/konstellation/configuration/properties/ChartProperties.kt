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
