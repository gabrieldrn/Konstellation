package com.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.charts.line.math.PathInterpolator
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.plotting.Dataset

/**
 * Converts a [dataset] into an unclosed [Path], with a [pathInterpolator] as a pre-processing step,
 * then draws it into the received [DrawScope].
 * @param dataset The dataset to be converted and drawn into a line chart.
 * @param pathInterpolator The interpolator to use as a pre-processing step before drawing the line.
 * @param lineStyle The drawing style to be applied to the drawn line chart path.
 */
internal fun DrawScope.drawLinePath(
    dataset: Dataset,
    pathInterpolator: PathInterpolator,
    lineStyle: LineDrawStyle,
) {
    drawLinePath(
        path = pathInterpolator(dataset),
        lineStyle = lineStyle
    )
}

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
