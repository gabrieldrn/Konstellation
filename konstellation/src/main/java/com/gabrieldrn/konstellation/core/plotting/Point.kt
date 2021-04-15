package com.gabrieldrn.konstellation.core.plotting

import androidx.compose.ui.geometry.Offset
import java.io.Serializable
import kotlin.math.abs

/**
 * Convenience type alias for a set of [Point]s.
 */
typealias Dataset = Collection<Point>

/**
 * A class representing a point in a chart.
 */
data class Point(

    /**
     * X point from the data set.
     */
    var x: Float,

    /**
     * Y point from the data set.
     */
    var y: Float,

    /**
     * Offset corresponding to [x] and [y] into the related chart. This attribute is managed by
     * Konstellation.
     */
    internal var offset: Offset = Offset(0f, 0f),

): Serializable {
    override fun toString() = "[($x;$y), $offset]"
}

/**
 * Creates a [Point] from this (as X value) and [that] (as Y value).
 */
infix fun Float.by(that: Float) = Point(this, that)

/**
 * Returns a new list of [Point]s.
 */
fun datasetOf(vararg elements: Point): Dataset =
    if (elements.isNotEmpty()) elements.asList() else emptyList()

/**
 * Returns a point with the nearest X-coordinate from the given position [x].
 */
fun Dataset.nearestPointByX(x: Float) = minByOrNull { abs(it.offset.x - x) }

/**
 * Converts a collection Float pairs to a list of [Point]s, assuming that the first element is X
 * and the second is Y.
 */
fun Collection<Pair<Float, Float>>.toDataset(): Dataset = map { it.first by it.second }

/**
 * A list of all the [Offset]s of every [Point] of this list. Please note offsets are managed by
 * Konstellation and may be uninitialized depending on the state of the chart calculations.
 */
val Dataset.offsets get() = map { it.offset }

/**
 * The highest X ([Point.x]) value of all the [Point]s from the current list.
 */
val Dataset.xMax get() = maxByOrNull { it.x }?.x ?: 0f

/**
 * The highest Y ([Point.y]) value of all the [Point]s from the current list.
 */
val Dataset.xMin get() = minByOrNull { it.x }?.x ?: 0f

/**
 * The lowest X ([Point.x]) value of all the [Point]s from the current list.
 */
val Dataset.yMax get() = maxByOrNull { it.y }?.y ?: 0f

/**
 * The lowest X ([Point.x]) value of all the [Point]s from the current list.
 */
val Dataset.yMin get() = minByOrNull { it.y }?.y ?: 0f

/**
 * A range from [xMin] to [xMax].
 */
val Dataset.xRange get() = xMin..xMax

/**
 * A range from [yMin] to [yMax].
 */
val Dataset.yRange get() = yMin..yMax
