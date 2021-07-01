package com.gabrieldrn.konstellation.util

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/**
 * This class computes the human-readable numbers for chart labels. After [compute] is executed,
 * the values [tickSpacing], [range], [niceMin], [niceMax] will be updated.
 * The algorithm used here is from the book "Graphics Gems, Volume 1" by Andrew S. Glassner,
 * chapter "Nice numbers for graph labels" page 61.
 *
 * @param minPoint Minimum data point on the axis.
 * @param maxPoint Maximum data point on the axis.
 * @param maxTicks (Optional) Number of desired ticks. Default = 5
 */
class NiceScale(var minPoint: Float, var maxPoint: Float, var maxTicks: Int = 5) {

    var tickSpacing = 0f
        private set
    var range = 0f
        private set
    var niceMin = 0f
        private set
    var niceMax = 0f
        private set

    init {
        compute()
    }

    /**
     * Computes and update values for tick spacing and nice minimum and maximum data points on the
     * axis.
     */
    fun compute() {
        range = niceNum(maxPoint - minPoint, false)
        tickSpacing = niceNum(range / (maxTicks - 1), true)
        niceMin = floor(minPoint / tickSpacing) * tickSpacing
        niceMax = ceil(maxPoint / tickSpacing) * tickSpacing
    }

    /**
     * Returns a "nice" number approximately equal to [range]. Rounds the number if [round] = true.
     * Takes the ceiling otherwise.
     *
     * @param range Data range
     * @param round Whether to round the result
     * @return A "nice" number to be used for the data range
     */
    private fun niceNum(range: Float, round: Boolean): Float {
        val exponent = floor(log10(range)) //nice, rounded fraction
        val fraction = range / 10F.pow(exponent) //exponent of range
        val niceFraction = if (round) { //fractional part of range
            when {
                fraction < 1.5f -> 1.0f
                fraction < 3f -> 2f
                fraction < 7f -> 5f
                else -> 10f
            }
        } else {
            when {
                fraction <= 1f -> 1f
                fraction <= 2f -> 2f
                fraction <= 5f -> 5f
                else -> 10f
            }
        }
        return niceFraction * 10f.pow(exponent)
    }

    override fun toString(): String {
        return "Tick spacing = $tickSpacing\n" +
                "Range = $range\n" +
                "nice min = $niceMin\n" +
                "nice max = $niceMax"
    }
}
