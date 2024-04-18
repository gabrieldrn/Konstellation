package dev.gabrieldrn.konstellation.charts.line.math

import androidx.compose.ui.graphics.Path
import dev.gabrieldrn.konstellation.plotting.Dataset

/**
 * Defines a class that interpolates a [Dataset] and returns its interpolation result as an unclosed
 * [Path], representing a line chart.
 */
public interface PathInterpolator {

    /**
     * Produces and returns a [Path] representing the interpolated [dataset].
     */
    public operator fun invoke(dataset: Dataset): Path
}
