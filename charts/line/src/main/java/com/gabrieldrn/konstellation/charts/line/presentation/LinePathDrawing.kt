package com.gabrieldrn.konstellation.charts.line.presentation

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

/**
 * Converts a received [Dataset] into an unclosed [Path] which plots the dataset into a line chart
 * path to be drawn.
 * @param smoothing The smoothing effect to be applied to the path between each data point.
 * @return The path representing the dataset into a line a chart.
 */
internal fun Dataset.toLinePath(smoothing: Smoothing) = Path().apply {
    var prev: Point
    forEachIndexed { i, p ->
        if (i == 0) {
            moveTo(p.xPos, p.yPos)
        } else when (smoothing) {
            Smoothing.None -> lineTo(p.xPos, p.yPos)
            Smoothing.CubicX -> {
                prev = get(i - 1)
                cubicTo(
                    x1 = p.xPos,
                    y1 = prev.yPos,
                    x2 = prev.xPos,
                    y2 = p.yPos,
                    x3 = p.xPos,
                    y3 = p.yPos,
                )
            }
            Smoothing.CubicY -> {
                prev = get(i - 1)
                cubicTo(
                    x1 = prev.xPos,
                    y1 = p.yPos,
                    x2 = p.xPos,
                    y2 = prev.yPos,
                    x3 = p.xPos,
                    y3 = p.yPos,
                )
            }
        }
    }
}

/**
 * Converts a [dataset] into a [Path] (see [toLinePath]) and draws it into the received [DrawScope].
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
        path = dataset.toLinePath(smoothing),
        lineStyle = lineStyle
    )
}

/**
 * Draws a given [path] (considered as a line chart path) into the received [DrawScope].
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
