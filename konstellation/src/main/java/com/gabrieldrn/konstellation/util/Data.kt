package com.gabrieldrn.konstellation.util

import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.core.plotting.by
import kotlin.random.Random
import kotlin.random.nextInt

val samplePoints = listOf(
    0f by 0f,
    25f by 50f,
    50f by 200f,
    75f by 150f,
    100f by 175f
)

/**
 * Generates a random data set of 25 points with a y value between `0f` and `1f`.
 */
fun randomDataSet() = mutableListOf<Point>().apply {
    (-8..12).forEach {
        add(it.toFloat() by Random.nextInt(-10..10).toFloat())
    }
}
