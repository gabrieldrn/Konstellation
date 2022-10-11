package com.gabrieldrn.konstellation.configuration.properties

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

    // TODO Complex cubic with quadratic Bézier curves
}
