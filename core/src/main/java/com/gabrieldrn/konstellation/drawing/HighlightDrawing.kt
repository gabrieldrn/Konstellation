package com.gabrieldrn.konstellation.drawing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.highlighting.verticalHLPositions
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.PointDrawStyle

/**
 * Highlights a given [point] in the chart by drawing another circle styled with
 * [pointStyle] in front of it and dashed lines based on [positions].
 */
fun DrawScope.highlightPoint(
    point: Point,
    positions: Set<HighlightPosition>,
    pointStyle: PointDrawStyle,
    lineStyle: LineDrawStyle
) {
    drawPoint(point, pointStyle)
    if (positions.any { it in verticalHLPositions }) {
        drawLine(
            Offset(point.offset.x, 0f),
            Offset(point.offset.x, size.height),
            lineStyle
        )
    }
    if (positions.any { it in horizontalHLPositions }) {
        drawLine(
            Offset(0f, point.yPos),
            Offset(size.width, point.yPos),
            lineStyle
        )
    }
}
