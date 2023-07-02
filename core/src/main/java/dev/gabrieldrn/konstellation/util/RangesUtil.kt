package dev.gabrieldrn.konstellation.util

import dev.gabrieldrn.konstellation.configuration.properties.DatasetOffsets

/**
 * Distance between the first and last value (end - start).
 */
public val ClosedFloatingPointRange<Float>.distance: Float get() = endInclusive - start

/**
 * Applies a receiving [DatasetOffsets] to X and Y from given drawing ranges then returns them
 * modified in a pair [x-range, y-range].
 */
public fun DatasetOffsets?.applyDatasetOffsets(
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
 * Returns a new [ClosedFloatingPointRange] with both start and end value increased by [other].
 */
public fun ClosedFloatingPointRange<Float>.plus(
    other: Float
): ClosedFloatingPointRange<Float> = start + other..endInclusive + other

/**
 * Returns a new [ClosedFloatingPointRange] with both start and end value increased by [other].
 */
public fun ClosedFloatingPointRange<Float>.minus(
    other: Float
): ClosedFloatingPointRange<Float> = start - other..endInclusive - other

/**
 * Returns a new [ClosedFloatingPointRange] with its distance increased by [other].
 */
public fun ClosedFloatingPointRange<Float>.inc(
    other: Float
): ClosedFloatingPointRange<Float> = start - other..endInclusive + other

/**
 * Returns a new [ClosedFloatingPointRange] with its distance decreased by [other].
 */
public fun ClosedFloatingPointRange<Float>.dec(
    other: Float
): ClosedFloatingPointRange<Float> = start + other..endInclusive - other

/**
 * Returns a new [ClosedFloatingPointRange] with start value increased by [other].
 * @throws IllegalArgumentException If increased start value is greater than end value.
 */
public fun ClosedFloatingPointRange<Float>.incStart(other: Float): ClosedFloatingPointRange<Float> {
    require(start + other <= endInclusive) {
        "Increase start value by $other would result in a value greater than end value."
    }
    return start + other..endInclusive
}

/**
 * Returns a new [ClosedFloatingPointRange] with end value increased by [other].
 * @throws IllegalArgumentException If increased end value is lower than start value.
 */
public fun ClosedFloatingPointRange<Float>.incEnd(other: Float): ClosedFloatingPointRange<Float> {
    require(endInclusive + other >= start) {
        "Increase end value by $other would result in a value lower than start value."
    }
    return start..endInclusive + other
}

/**
 * Returns a new [ClosedFloatingPointRange] with start value decreased by [other].
 * @throws IllegalArgumentException If increased start value is greater than end value.
 */
public fun ClosedFloatingPointRange<Float>.decStart(other: Float): ClosedFloatingPointRange<Float> {
    require(start - other <= endInclusive) {
        "Decrease start value by $other would result in a value greater than end value."
    }
    return start - other..endInclusive
}

/**
 * Returns a new [ClosedFloatingPointRange] with end value decreased by [other].
 * @throws IllegalArgumentException If increased end value is lower than start value.
 */
public fun ClosedFloatingPointRange<Float>.decEnd(other: Float): ClosedFloatingPointRange<Float> {
    require(endInclusive - other >= start) {
        "Decrease end value by $other would result in a value lower than start value."
    }
    return start..endInclusive - other
}
