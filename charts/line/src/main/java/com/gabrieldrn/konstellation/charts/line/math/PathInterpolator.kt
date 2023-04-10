package com.gabrieldrn.konstellation.charts.line.drawing

import androidx.compose.ui.graphics.Path
import com.gabrieldrn.konstellation.plotting.Dataset

/**
 * Defines a class that interpolates a [Dataset] and returns its interpolation result as an unclosed
 * [Path], representing a line chart.
 */
interface PathInterpolator {

    /**
     * Produces and returns a [Path] representing the interpolated [dataset].
     */
    operator fun invoke(dataset: Dataset): Path
}
