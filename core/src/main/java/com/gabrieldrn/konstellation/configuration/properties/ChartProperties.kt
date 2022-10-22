package com.gabrieldrn.konstellation.configuration.properties

import com.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of properties that can be applied to a chart.
 * @property axes Axes to be drawn on the chart.
 * @property smoothing Rendering method for the drawn data.
 * @property drawFrame Either to draw the lines delimiting the chart or not.
 * @property drawZeroLines Either to draw the lines indicating the zero on X and Y axes or not.
 */
interface ChartProperties {
    val axes: Set<ChartAxis>
    val smoothing: Smoothing
    val drawFrame: Boolean
    val drawZeroLines: Boolean
}
