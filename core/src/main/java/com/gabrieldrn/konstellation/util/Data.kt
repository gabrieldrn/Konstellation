@file:Suppress("MagicNumber", "ForEachOnRange")
package com.gabrieldrn.konstellation.util

import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.by
import kotlin.random.Random
import kotlin.random.nextInt

internal val samplePoints = listOf(
    0f by 0f,
    25f by 50f,
    50f by 200f,
    75f by 150f,
    100f by 175f
)

/**
 * Generates a random data set of 20 points with a y value between `-10f` and `10f`.
 */
fun randomDataSet() = mutableListOf<Point>().apply {
    val range = 10
    (-range..range).forEach {
        add(it.toFloat() by Random.nextInt(-range..range).toFloat())
    }
} as Dataset

/**
 * Generates a random fancy data set of 100 points with a y value between `0f` and `1000f`,
 * stepped by 100.
 */
fun randomFancyDataSet() = mutableListOf<Point>().apply {
    val range = 0..10
    var y = 0
    repeat(100) {
        y = Random.nextInt(y-1..y+1).coerceIn(range)
        add((it+1).toFloat() by y.times(100).toFloat())
    }
} as Dataset
