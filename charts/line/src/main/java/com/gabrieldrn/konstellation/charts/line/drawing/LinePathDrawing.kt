package com.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.plotting.Dataset

/**
 * Converts a [dataset] into an unclosed [Path], with a pre-processing [smoothing] step, then draws
 * it into the received [DrawScope].
 * @param dataset The dataset to be converted and drawn into a line chart.
 * @param smoothing The smoothing effect to be applied to the path between each data point.
 * @param lineStyle The drawing style to be applied to the drawn line chart path.
 */
internal fun DrawScope.drawLinePath(
    dataset: Dataset,
    smoothing: Smoothing,
    lineStyle: LineDrawStyle,
) {
    drawLinePath(
        path = smoothing.interpolator(dataset),
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
