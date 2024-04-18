@file:Suppress("MagicNumber", "ForEachOnRange")
package dev.gabrieldrn.konstellation.util

import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.Point
import dev.gabrieldrn.konstellation.plotting.asDataset
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import kotlin.random.Random
import kotlin.random.nextInt

internal val samplePoints = datasetOf(
    0f by 0f,
    25f by 50f,
    50f by 200f,
    75f by 150f,
    100f by 175f
)

/**
 * Generates a random data set of 20 points with a y value between `-10f` and `10f`.
 */
public fun randomDataSet(): Dataset = mutableListOf<Point>().apply {
    val range = 10
    (-range..range).forEach {
        add(it.toFloat() by Random.nextInt(-range..range).toFloat())
    }
}.asDataset()

/**
 * Generates a random fancy data set of 15 points with a y value between `0f` and `10f`,
 * stepped by 1.
 */
public fun randomFancyDataSet(): Dataset = mutableListOf<Point>().apply {
    val range = 0..10
    var y = 0
    repeat(16) {
        y = Random.nextInt(y-1..y+1).coerceIn(range)
        add(it.toFloat() by y.times(1).toFloat())
    }
}.asDataset()
