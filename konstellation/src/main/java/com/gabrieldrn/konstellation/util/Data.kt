package com.gabrieldrn.konstellation.util

import com.gabrieldrn.konstellation.core.plotting.Vertex
import com.gabrieldrn.konstellation.core.plotting.by
import kotlin.random.Random

val samplePoints = arrayOf(
    0f by 0f,
    25f by 50f,
    50f by 200f,
    75f by 150f,
    100f by 175f
)

/**
 * Generates a random data set of 25 points with a y value between `0f` and `1f`.
 */
fun randomDataSet() = mutableListOf<Vertex>().apply {
    repeat(25) {
        add(it.toFloat() by Random.nextFloat())
    }
}.toTypedArray()
