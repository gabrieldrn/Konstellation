package com.gabrieldrn.konstellation.drawing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.highlighting.verticalHLPositions
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle

/**
 * Highlights a given [point] in the chart by drawing another circle styled with
 * [highlightPointStyle] in front of it and dashed lines based on [highlightPositions].
 */
fun DrawScope.highlightPoint(
    point: Point,
    highlightPositions: Array<HighlightPosition>,
    highlightPointStyle: PointDrawStyle,
    highlightLineStyle: LineDrawStyle
) {
    drawPoint(point, highlightPointStyle)
    if (highlightPositions.any { it in verticalHLPositions }) {
        drawLine(
            Offset(point.offset.x, 0f),
            Offset(point.offset.x, size.height),
            highlightLineStyle
        )
    }
    if (highlightPositions.any { it in horizontalHLPositions }) {
        drawLine(
            Offset(0f, point.yPos),
            Offset(size.width, point.yPos),
            highlightLineStyle
        )
    }
}