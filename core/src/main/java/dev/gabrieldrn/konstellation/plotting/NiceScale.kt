package dev.gabrieldrn.konstellation.plotting

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/**
 * This class computes the human-readable numbers for chart labels with a logarithmic method.
 * After [compute] is executed, the values [tickSpacing], [axisRange], [niceMin], [niceMax] will be
 * updated.
 *
 * @property dataRange Range of the data to compute tickers from.
 * @property maxTicks (Optional) Number of desired ticks. Default = 5
 */
public class NiceScale(
    public var dataRange: ClosedFloatingPointRange<Float>,
    public var maxTicks: Int = 5
) {

    /**
     * Range of the axis.
     */
    private var axisRange = 0f

    /**
     * Spacing between each tick.
     */
    public var tickSpacing: Float = 0f
        private set

    /**
     * Nice minimum value.
     */
    public var niceMin: Float = 0f
        private set

    /**
     * Nice maximum value.
     */
    public var niceMax: Float = 0f
        private set

    init {
        compute()
    }

    /**
     * Computes and update values for tick spacing and nice minimum and maximum data points on the
     * axis.
     */
    public fun compute() {
        axisRange = niceNum(dataRange.endInclusive - dataRange.start, false)
        tickSpacing = niceNum(axisRange / (maxTicks - 1), true)
        niceMin = floor(dataRange.start / tickSpacing) * tickSpacing
        niceMax = ceil(dataRange.endInclusive / tickSpacing) * tickSpacing
    }

    /**
     * Returns a "nice" number approximately equal to [range]. Rounds the number if [round] = true.
     * Takes the ceiling otherwise.
     *
     * @param range Data range.
     * @param round Whether to round the result.
     * @return A "nice" number to be used for the data range.
     */
    @Suppress("MagicNumber")
    private fun niceNum(range: Float, round: Boolean): Float {
        val exponent = floor(log10(range)) //nice, rounded fraction
        val fraction = range / 10f.pow(exponent) //exponent of range
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
                "Range = $axisRange\n" +
                "nice min = $niceMin\n" +
                "nice max = $niceMax"
    }
}
