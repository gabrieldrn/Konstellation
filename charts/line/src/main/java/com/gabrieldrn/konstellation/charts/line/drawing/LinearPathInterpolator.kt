package com.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.*
import com.gabrieldrn.konstellation.plotting.Dataset

/**
 * Interpolates with a common linear interpolation (lerp), resulting in straight lines between each
 * data point.
 */
class LinearPathInterpolator : PathInterpolator {

    override fun invoke(dataset: Dataset) = Path().apply {
        dataset.forEachIndexed { i, p ->
            if (i == 0) moveTo(p.xPos, p.yPos)
            else lineTo(p.xPos, p.yPos)
        }
    }
}
