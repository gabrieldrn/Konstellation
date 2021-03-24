package com.gabrieldrn.konstellation.core.plotting

import androidx.compose.ui.geometry.Offset

typealias Dataset = Collection<Point>

/**
 * A class representing a point in a chart.
 */
data class Point(
    var x: Float,
    var y: Float,
    var offset: Offset = Offset(0f, 0f),
)

val Collection<Point>.offsets get() = map { it.offset }
val Collection<Point>.xMax get() = map { it.x }.maxOrNull() ?: 0f
val Collection<Point>.xMin get() = map { it.x }.minOrNull() ?: 0f
val Collection<Point>.yMax get() = map { it.y }.maxOrNull() ?: 0f
val Collection<Point>.yMin get() = map { it.y }.minOrNull() ?: 0f
val Collection<Point>.xRange get() = xMin..xMax
val Collection<Point>.yRange get() = yMin..yMax

infix fun Float.by(that: Float): Point = Point(this, that)
