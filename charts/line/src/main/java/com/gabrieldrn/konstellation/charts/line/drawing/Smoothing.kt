package com.gabrieldrn.konstellation.charts.line.drawing

/**
 * How plotting shall render.
 */
enum class Smoothing(val interpolator: PathInterpolator) {

    /**
     * The drawing will result in a polygonal chain (straight lines between each point).
     */
    Linear(LinearPathInterpolator()),

    /**
     * Drawing between each data points will use a simple cubic Bézier curve effect on the X axis,
     * without adaptation from the positions of the other data points.
     */
    CubicX(CubicXPathInterpolator()),

    /**
     * Drawing between each data points will use a simple cubic Bézier curve effect on the Y axis,
     * without adaptation from the positions of the other data points.
     */
    CubicY(CubicYPathInterpolator()),

    /**
     * The drawing will result in a cubic spline preserving monotonicity in Y while assuming X is
     * monotonic, as proposed by M. Steffen in
     * [A Simple Method for Monotonic Interpolation in One Dimension](https://adsabs.harvard.edu/full/1990A%26A...239..443S).
     */
    MonotonicX(MonotoneXPathInterpolator());
}
