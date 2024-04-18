package dev.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.*
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle

/**
 * Draws a [path] according to a [lineStyle].
 * @param path The path to be drawn.
 * @param lineStyle The drawing style to be applied to the drawn line chart path.
 */
internal fun DrawScope.drawLinePath(
    path: Path,
    lineStyle: LineDrawStyle,
) {
    with(lineStyle) {
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth.toPx(),
                join = StrokeJoin.Round,
                cap = cap,
                pathEffect = if (dashed) {
                    PathEffect.dashPathEffect(dashedEffectIntervals, dashedEffectPhase)
                } else null
            )
        )
    }
}
