package dev.gabrieldrn.konstellation.plotting

import androidx.compose.ui.geometry.Offset
import java.io.Serializable
import kotlin.math.abs

/**
 * Convenience type alias for a set of [Point]s.
 */
public typealias Dataset = List<Point>

/**
 * A class representing a point in a chart.
 */
public data class Point constructor(

    /**
     * X point value from the data set.
     */
    val x: Float,

    /**
     * Y point value from the data set.
     */
    val y: Float,

    /**
     * Offset corresponding to the position of the point into a chart.
     */
    val offset: Offset = Offset(0f, 0f),

): Serializable {

    /**
     * Returns the x value of [offset].
     */
    public val xPos: Float get() = offset.x

    /**
     * Returns the y value of [offset].
     */
    public val yPos: Float get() = offset.y

    override fun toString(): String = "[($x;$y), $offset]"

    public companion object {
        /** Required by [Serializable]. */
        private const val serialVersionUID = 1L
    }
}

/**
 * Creates a [Point] from this (as X value) and [that] (as Y value).
 */
public infix fun Float.by(that: Float): Point = Point(this, that)

/**
 * Returns a new list of [Point]s.
 */
public fun datasetOf(vararg elements: Point): Dataset =
    if (elements.isNotEmpty()) elements.asList() else emptyList()

/**
 * Casts this list of [Point]s as a [Dataset].
 */
public fun List<Point>.asDataset(): Dataset = this

/**
 * Converts a collection Float pairs to a list of [Point]s, assuming that the first element is X
 * and the second is Y.
 */
public fun Collection<Pair<Float, Float>>.toDataset(): Dataset = map { it.first by it.second }

/**
 * Checks if this [Dataset] is valid. The validation checks for:
 *  - Non-emptiness
 *  - Monotonicity on the x-axis (xi < xi+1)
 *  - Duplicate values on the x-axis
 *  - NaN values
 *  - Infinite values
 *
 *  @throws IllegalArgumentException If the dataset is invalid.
 */
public fun Dataset.validate() {
    require(isNotEmpty()) { "Dataset must not be empty" }
    // Check for duplicates on the x-axis
    require(map { it.x }.distinct().size == size) {
        "Dataset must not have duplicate values on the x-axis"
    }
    // Check monotonicity
    require(zipWithNext().all { (a, b) -> a.x < b.x }) {
        "Dataset must be monotonically increasing on the x-axis"
    }
    // Check for NaNs
    require(all { !it.x.isNaN() && !it.y.isNaN() }) {
        "Dataset must not have NaN values"
    }
    // Check for infinite values
    require(all { !it.x.isInfinite() && !it.y.isInfinite() }) {
        "Dataset must not have infinite values"
    }
}

/**
 * A list of all the [Offset]s of every [Point] of this list. Please note offsets are managed by
 * Konstellation and may be uninitialized depending on the state of the chart calculations and
 * composition.
 */
public val Dataset.offsets: List<Offset> get() = map { it.offset }

/**
 * Returns a point with the nearest X-coordinate from the given position [x].
 */
public fun Dataset.nearestPointByX(x: Float): Point? = minByOrNull { abs(it.offset.x - x) }

/**
 * The highest X ([Point.x]) value of all the [Point]s from the current list.
 */
public val Dataset.xMax: Float get() = maxByOrNull { it.x }?.x ?: 0f

/**
 * The highest Y ([Point.y]) value of all the [Point]s from the current list.
 */
public val Dataset.xMin: Float get() = minByOrNull { it.x }?.x ?: 0f

/**
 * The lowest X ([Point.x]) value of all the [Point]s from the current list.
 */
public val Dataset.yMax: Float get() = maxByOrNull { it.y }?.y ?: 0f

/**
 * The lowest X ([Point.x]) value of all the [Point]s from the current list.
 */
public val Dataset.yMin: Float get() = minByOrNull { it.y }?.y ?: 0f

/**
 * A range from [xMin] to [xMax].
 */
public val Dataset.xRange: ClosedFloatingPointRange<Float> get() = xMin..xMax

/**
 * A range from [yMin] to [yMax].
 */
public val Dataset.yRange: ClosedFloatingPointRange<Float> get() = yMin..yMax
