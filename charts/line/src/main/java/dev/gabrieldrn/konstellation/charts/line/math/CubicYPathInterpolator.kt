package dev.gabrieldrn.konstellation.charts.line.math

import androidx.compose.ui.graphics.Path
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.Point

/**
 * Produces a simple cubic Bézier curve effect on the Y axis between each data point.
 */
public class CubicYPathInterpolator : PathInterpolator {

    override fun invoke(dataset: Dataset): Path = Path().apply {
        var prev: Point
        dataset.forEachIndexed { i, p ->
            if (i == 0) {
                moveTo(p.xPos, p.yPos)
            } else {
                prev = dataset[i - 1]
                cubicTo(
                    x1 = prev.xPos, y1 = p.yPos,
                    x2 = p.xPos, y2 = prev.yPos,
                    x3 = p.xPos, y3 = p.yPos,
                )
            }
        }
    }
}
