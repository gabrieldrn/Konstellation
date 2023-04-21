package com.gabrieldrn.konstellation.charts.line.math

import androidx.compose.ui.graphics.Path
import com.gabrieldrn.konstellation.plotting.Dataset

/**
 * Interpolates with a common linear interpolation (lerp), resulting in straight lines between each
 * data point.
 */
public class LinearPathInterpolator : PathInterpolator {

    override fun invoke(dataset: Dataset): Path = Path().apply {
        dataset.forEachIndexed { i, p ->
            if (i == 0) {
                moveTo(p.xPos, p.yPos)
            } else {
                lineTo(p.xPos, p.yPos)
            }
        }
    }
}
