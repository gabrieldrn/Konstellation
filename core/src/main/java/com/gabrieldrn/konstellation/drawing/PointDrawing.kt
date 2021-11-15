package com.gabrieldrn.konstellation.drawing

import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.style.PointDrawStyle

/**
 * Draws a circle representing a point of the dataset
 */
internal fun DrawScope.drawPoint(point: Point, style: PointDrawStyle = PointDrawStyle()) {
    drawCircle(center = point.offset, color = style.color, radius = style.radius.toPx())
}