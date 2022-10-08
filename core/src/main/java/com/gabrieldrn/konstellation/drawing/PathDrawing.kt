package com.gabrieldrn.konstellation.drawing

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import com.gabrieldrn.konstellation.configuration.properties.Rounding
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

fun DrawScope.drawPath(
    dataset: Dataset,
    lineStyle: LineDrawStyle,
    rounding: Rounding
) {
    drawPath(
        path = Path().apply {
            var prev: Point
            dataset.forEachIndexed { i, p ->
                if (i == 0) {
                    moveTo(p.offset.x, p.offset.y)
                } else when (rounding) {
                    Rounding.None -> lineTo(p.offset.x, p.offset.y)
                    Rounding.SimpleCubic -> {
                        prev = dataset[i - 1]
                        cubicTo(
                            x1 = p.offset.x,
                            y1 = prev.offset.y,
                            x2 = prev.offset.x,
                            y2 = p.offset.y,
                            x3 = p.offset.x,
                            y3 = p.offset.y,
                        )
                    }
                }
            }
        },
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
