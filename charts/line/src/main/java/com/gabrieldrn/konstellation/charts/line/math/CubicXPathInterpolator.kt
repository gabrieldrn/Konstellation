package com.gabrieldrn.konstellation.charts.line.math

import androidx.compose.ui.graphics.Path
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

/**
 * Produces a simple cubic Bézier curve effect on the X axis between each data point.
 */
public class CubicXPathInterpolator : PathInterpolator {

    override fun invoke(dataset: Dataset): Path = Path().apply {
        var prev: Point
        dataset.forEachIndexed { i, p ->
            if (i == 0) {
                moveTo(p.xPos, p.yPos)
            } else {
                prev = dataset[i - 1]
                cubicTo(
                    x1 = p.xPos, y1 = prev.yPos,
                    x2 = prev.xPos, y2 = p.yPos,
                    x3 = p.xPos, y3 = p.yPos,
                )
            }
        }
    }
}
