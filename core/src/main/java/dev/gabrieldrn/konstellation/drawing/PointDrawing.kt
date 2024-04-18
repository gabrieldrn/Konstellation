package dev.gabrieldrn.konstellation.drawing

import androidx.compose.ui.graphics.drawscope.*
import dev.gabrieldrn.konstellation.plotting.Point
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle

/**
 * Draws a circle representing a point of the dataset.
 */
public fun DrawScope.drawPoint(point: Point, style: PointDrawStyle = PointDrawStyle()) {
    drawCircle(center = point.offset, color = style.color, radius = style.radius.toPx())
}
