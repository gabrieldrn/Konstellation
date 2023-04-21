package com.gabrieldrn.konstellation.charts.line.math

import androidx.compose.ui.graphics.Path
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.by
import kotlin.math.abs
import kotlin.math.sign

/**
 * Produces a monotonic cubic Hermite spline through a given dataset, preserving monotonicity in Y
 * while assuming X is monotonic
 * [as proposed by M. Steffen](https://adsabs.harvard.edu/full/1990A%26A...239..443S).
 * This implementation is inspired by the
 * [D3-shape.js](https://github.com/d3/d3-shape/blob/main/src/curve/monotone.js) library.
 */
@Suppress(
    "MagicNumber",
    "VariableNaming" // No real gain in renaming variables to comply with naming conventions.
)
public class MonotoneXPathInterpolator : PathInterpolator {

    override operator fun invoke(dataset: Dataset): Path = Path().apply {

        /**
         * Appends a cubic Hermite spline from [p0] to [p1] with the given tangents [t0] and [t1] to
         * this path.
         */
        fun hermiteCubicTo(p0: Point, p1: Point, t0: Float, t1: Float) {
            val dx = (p1.xPos - p0.xPos) / 3
            cubicTo(
                p0.xPos + dx, p0.yPos + dx * t0,
                p1.xPos - dx, p1.yPos - dx * t1,
                p1.xPos, p1.yPos
            )
        }

        var p0 = 0f by 0f
        var p1 = 0f by 0f
        var t0 = 0f
        var i = 0

        for (p in dataset) {
            var t1 = 0f

            if (p.xPos == p1.xPos && p.yPos == p1.yPos) continue // Ignore coincident points.

            when (i) {
                0 -> moveTo(p.xPos, p.yPos)
                1 -> {}
                2 -> {
                    t1 = slope3(p0, p1, p)
                    hermiteCubicTo(p0, p1, t1, t1)
                }
                else -> {
                    t1 = slope3(p0, p1, p)
                    hermiteCubicTo(p0, p1, t0, t1)
                }
            }

            p0 = p1
            p1 = p
            t0 = t1

            i++
        }

        val p = dataset.last()
        if (i == 2) {
            lineTo(p.xPos, p.yPos)
        } else {
            hermiteCubicTo(p0, p1, t0, slope2(p0, p1, t0))
        }
    }

    /**
     * Returns the slope of the tangents. The slope is defined as the tangent of the angle between
     * the line and the horizontal axis.
     * @param p0 The first point of the line.
     * @param p1 The second point of the line.
     * @param p2 The third point of the line.
     */
    private fun slope3(p0: Point, p1: Point, p2: Point): Float {
        val h0 = p1.xPos - p0.xPos
        val h1 = p2.xPos - p1.xPos
        val s0 = (p1.yPos - p0.yPos) / h0.coerceAtLeast(h1 * -1)
        val s1 = (p2.yPos - p1.yPos) / h1.coerceAtLeast(h0 * -1)
        val p = (s0 * h1 + s1 * h0) / (h0 + h1)
        return (s0.sign + s1.sign) * minOf(abs(s0), abs(s1), 0.5f * abs(p))
    }

    /**
     * Returns the slope of the tangent. The slope is defined as the tangent of the angle between
     * the line and the horizontal axis.
     * @param p0 The first point of the line.
     * @param p1 The second point of the line.
     * @param t The slope of the tangent of the previous line.
     */
    private fun slope2(p0: Point, p1: Point, t: Float): Float {
        val h = p1.xPos - p0.xPos
        return if (h != 0f) (3 * (p1.yPos - p0.yPos) / h - t) / 2 else t
    }
}
