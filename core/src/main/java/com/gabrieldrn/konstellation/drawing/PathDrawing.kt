package com.gabrieldrn.konstellation.drawing

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.configuration.properties.Rounding
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

fun Dataset.toPath(rounding: Rounding) = Path().apply {
    var prev: Point
    forEachIndexed { i, p ->
        if (i == 0) {
            moveTo(p.xPos, p.yPos)
        } else when (rounding) {
            Rounding.None -> lineTo(p.xPos, p.yPos)
            Rounding.SimpleCubic -> {
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
        }
    }
}

fun DrawScope.drawPath(
    dataset: Dataset,
    rounding: Rounding,
    lineStyle: LineDrawStyle,
) {
    drawPath(
        path = dataset.toPath(rounding),
        lineStyle = lineStyle
    )
}

fun DrawScope.drawPath(
    path: Path,
    lineStyle: LineDrawStyle,
) {
    drawPath(
        path = path,
        color = lineStyle.color,
        style = Stroke(
            width = lineStyle.strokeWidth.toPx(),
            join = StrokeJoin.Round,
            cap = lineStyle.cap,
            pathEffect = if (lineStyle.dashed) {
                PathEffect.dashPathEffect(
                    lineStyle.dashedEffectIntervals,
                    lineStyle.dashedEffectPhase
                )
            } else null
        )
    )
}
