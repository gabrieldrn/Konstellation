package com.gabrieldrn.konstellation.core.plotting

/**
 * A class representing a point in a chart.
 */
data class Vertex(val x: Float, val y: Float)

infix fun Float.by(that: Float): Vertex = Vertex(this, that)
