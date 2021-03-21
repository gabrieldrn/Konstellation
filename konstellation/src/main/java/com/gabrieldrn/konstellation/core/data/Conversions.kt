package com.gabrieldrn.konstellation.core.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.gabrieldrn.konstellation.core.plotting.Vertex

/**
 * Converts an array of [Vertex]s to a list of [Offset].
 */
internal fun DrawScope.offsetsFromPoints(
    points: Array<Vertex>
): List<Offset> {
    val xMax = points.map { it.x }.maxOrNull() ?: 0f
    val yMax = points.map { it.y }.maxOrNull() ?: 0f
    return points.map {
        Offset(
            x = (it.x / xMax) * size.width,
            y = ((yMax - it.y) * size.height) / yMax
        )
    }
}
