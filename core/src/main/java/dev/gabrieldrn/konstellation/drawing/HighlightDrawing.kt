package dev.gabrieldrn.konstellation.drawing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import dev.gabrieldrn.konstellation.highlighting.verticalHLPositions
import dev.gabrieldrn.konstellation.plotting.Point
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import dev.gabrieldrn.konstellation.highlighting.HighlightLinePosition

/**
 * Highlights a given [point] in the chart by drawing another circle styled with
 * [pointStyle] in front of it and dashed lines based on [contentPositions] and/or [linePosition].
 */
public fun DrawScope.highlightPoint(
    point: Point,
    contentPositions: Set<HighlightContentPosition>,
    linePosition: HighlightLinePosition?,
    pointStyle: PointDrawStyle,
    lineStyle: LineDrawStyle?
) {
    drawPoint(point, pointStyle)

    if (lineStyle == null || linePosition == null) return

    fun horizontalLine() = drawLine(
        Offset(0f, point.yPos),
        Offset(size.width, point.yPos),
        lineStyle
    )

    fun verticalLine() = drawLine(
        Offset(point.offset.x, 0f),
        Offset(point.offset.x, size.height),
        lineStyle
    )

    when (linePosition) {
        HighlightLinePosition.Relative -> {
            if (contentPositions.any { it in verticalHLPositions }) {
                verticalLine()
            }
            if (contentPositions.any { it in horizontalHLPositions }) {
                horizontalLine()
            }
        }
        HighlightLinePosition.Horizontal -> horizontalLine()
        HighlightLinePosition.Vertical -> verticalLine()
        HighlightLinePosition.Both -> { horizontalLine(); verticalLine() }
    }
}
