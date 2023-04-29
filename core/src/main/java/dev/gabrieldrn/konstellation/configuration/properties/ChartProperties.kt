package dev.gabrieldrn.konstellation.configuration.properties

/**
 * Base definition of properties that can be applied to a chart.
 */
public interface ChartProperties {

    /**
     * If true, any highlight change shall perform a haptic feedback.
     */
    public val hapticHighlight: Boolean
}
