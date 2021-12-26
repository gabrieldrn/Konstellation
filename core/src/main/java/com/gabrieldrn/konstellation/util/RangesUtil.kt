package com.gabrieldrn.konstellation.util

import com.gabrieldrn.konstellation.style.DatasetOffsets

/**
 * Distance between the first and last value (end - start)
 */
val ClosedFloatingPointRange<Float>.rawRange get() = endInclusive - start

/**
 * Applies a receiving [DatasetOffsets] to X and Y given drawing ranges then returns them modified
 * in a pair [x-range, y-range].
 */
fun DatasetOffsets?.applyDatasetOffsets(
    xDrawRange: ClosedFloatingPointRange<Float>,
    yDrawRange: ClosedFloatingPointRange<Float>,
): Pair<ClosedFloatingPointRange<Float>, ClosedFloatingPointRange<Float>> {
    if (this == null) return xDrawRange to yDrawRange
    var newXRange = xDrawRange
    var newYRange = yDrawRange
    xEndOffset?.let { newXRange = newXRange.decStart(it) }
    xEndOffset?.let { newXRange = newXRange.incEnd(it) }
    yStartOffset?.let { newYRange = newYRange.decStart(it) }
    yEndOffset?.let { newYRange = newYRange.incEnd(it) }
    return newXRange to newYRange
}

/**
 * Returns a new [ClosedFloatingPointRange] with start value increased by [other].
 * @throws IllegalArgumentException If increased start value is greater than end value.
 */
fun ClosedFloatingPointRange<Float>.incStart(other: Float): ClosedFloatingPointRange<Float> {
    require(start + other <= endInclusive)
    return (start + other)..endInclusive
}

/**
 * Returns a new [ClosedFloatingPointRange] with end value increased by [other].
 * @throws IllegalArgumentException If increased end value is lower than start value.
 */
fun ClosedFloatingPointRange<Float>.incEnd(other: Float): ClosedFloatingPointRange<Float> {
    require(endInclusive + other >= start)
    return start..(endInclusive + other)
}

/**
 * Returns a new [ClosedFloatingPointRange] with start value decreased by [other].
 * @throws IllegalArgumentException If increased start value is greater than end value.
 */
fun ClosedFloatingPointRange<Float>.decStart(other: Float): ClosedFloatingPointRange<Float> {
    require(start - other <= endInclusive)
    return (start - other)..endInclusive
}

/**
 * Returns a new [ClosedFloatingPointRange] with end value decreased by [other].
 * @throws IllegalArgumentException If increased end value is lower than start value.
 */
fun ClosedFloatingPointRange<Float>.decEnd(other: Float): ClosedFloatingPointRange<Float> {
    require(endInclusive - other >= start)
    return start..(endInclusive - other)
}
