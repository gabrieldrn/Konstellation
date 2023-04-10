package com.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.*
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point

class CubicXPathInterpolator : PathInterpolator {

    override fun invoke(dataset: Dataset) = Path().apply {
        var prev: Point
        dataset.forEachIndexed { i, p ->
            if (i == 0) moveTo(p.xPos, p.yPos)
            else {
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
