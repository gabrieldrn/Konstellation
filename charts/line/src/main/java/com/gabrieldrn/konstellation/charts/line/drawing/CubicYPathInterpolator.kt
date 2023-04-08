package com.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.*
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

class CubicYPathInterpolator : PathInterpolator {

    override fun invoke(dataset: Dataset) = Path().apply {
        var prev: Point
        dataset.forEachIndexed { i, p ->
            if (i == 0) moveTo(p.xPos, p.yPos)
            else {
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
