package dev.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import dev.gabrieldrn.konstellation.charts.line.limitline.LimitLine
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.drawing.drawLabel
import dev.gabrieldrn.konstellation.drawing.drawLine
import dev.gabrieldrn.konstellation.math.map
import dev.gabrieldrn.konstellation.plotting.Axis
import dev.gabrieldrn.konstellation.plotting.Axis.Companion.isHorizontal
import dev.gabrieldrn.konstellation.plotting.Axis.Companion.isVertical

/**
 * Draws a [limitLine] on the current chart. The line won't be drawn if the value is outside the
 * current chart window.
 * @param limitLine The limit line to draw.
 * @param chartWindow The current chart window.
 */
public fun DrawScope.drawLimitLine(
    limitLine: LimitLine,
    chartWindow: ChartWindow
) {
    when {
        limitLine.axis.isHorizontal && limitLine.value in chartWindow.xWindow -> {
            val x = limitLine.value map (chartWindow.xWindow to 0f..size.width)
            drawLine(Offset(x, 0f), Offset(x, size.height), limitLine.style)
            limitLine.label?.let { label ->
                drawLabel(
                    label,
                    limitLine.axis,
                    Offset(
                        x = x,
                        y = when (limitLine.axis) {
                            Axis.X_BOTTOM -> size.height
                            Axis.X_TOP -> 0f
                            else -> error("Invalid axis")
                        }
                    )
                )
            }
        }
        limitLine.axis.isVertical && limitLine.value in chartWindow.yWindow -> {
            val y = limitLine.value map (chartWindow.yWindow to size.height..0f)
            drawLine(Offset(0f, y), Offset(size.width, y), limitLine.style)
            limitLine.label?.let { label ->
                drawLabel(
                    label,
                    limitLine.axis,
                    Offset(
                        x = when (limitLine.axis) {
                            Axis.Y_LEFT -> 0f
                            Axis.Y_RIGHT -> size.width
                            else -> error("Invalid axis")
                        },
                        y = y
                    )
                )
            }
        }
    }
}
