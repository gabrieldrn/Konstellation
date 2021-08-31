package com.gabrieldrn.konstellation.util

import com.gabrieldrn.konstellation.core.plotting.Dataset
import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.core.plotting.by
import kotlin.random.Random
import kotlin.random.nextInt

@Suppress("MagicNumber")
val samplePoints = listOf(
    0f by 0f,
    25f by 50f,
    50f by 200f,
    75f by 150f,
    100f by 175f
)

/**
 * Generates a random data set of 20 points with a y value between `-10f` and `10f`.
 */
@Suppress("MagicNumber")
fun randomDataSet() = mutableListOf<Point>().apply {
    (-10..10).forEach {
        add(it.toFloat() by Random.nextInt(-10..10).toFloat())
    }
} as Dataset

/**
 * Generates a random fancy data set of 20 points with a y value between `-10f` and `10f`.
 */
@Suppress("MagicNumber")
fun randomFancyDataSet() = mutableListOf<Point>().apply {
    val range = 10
    var y = Random.nextInt(-range..range)
    (-10..10).forEach {
        y = Random.nextInt(y-1..y+1).coerceIn(-range..range)
        add(it.toFloat() by y.toFloat())
    }
} as Dataset
