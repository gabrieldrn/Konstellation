package com.gabrieldrn.konstellation.configuration.properties

import com.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * Base definition of properties that can be applied to a chart.
 */
interface ChartProperties {

    /**
     * Axes to be drawn on the chart.
     */
    val axes: Set<ChartAxis>

    /**
     * Either to draw the lines delimiting the chart or not.
     */
    val drawFrame: Boolean

    /**
     * Either to draw the lines indicating the zero on X and Y axes or not.
     */
    val drawZeroLines: Boolean

    /**
     * If true, any highlight change shall perform a haptic feedback.
     */
    val hapticHighlight: Boolean
}
