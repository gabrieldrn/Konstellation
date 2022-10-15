package com.gabrieldrn.konstellation.configuration.properties

/**
 * How plotting shall render
 */
enum class Smoothing {
    /**
     * Drawing between data points will be straight, for example, in a line chart, the lines between
     * data points will be straight lines.
     */
    None,

    /**
     * Drawing between each data points will use a simple cubic Bézier curve effect on the X axis,
     * without adaptation from the positions of the other data points.
     */
    CubicX,

    /**
     * Drawing between each data points will use a simple cubic Bézier curve effect on the Y axis,
     * without adaptation from the positions of the other data points.
     */
    CubicY

    // TODO Complex cubic with quadratic Bézier curves
}
