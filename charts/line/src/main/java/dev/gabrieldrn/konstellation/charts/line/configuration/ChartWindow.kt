package dev.gabrieldrn.konstellation.charts.line.configuration

import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.xRange
import dev.gabrieldrn.konstellation.plotting.yRange

/**
 * Defines a window of visualization for the chart. The values of the window are expressed in
 * units of the chart. For example, if the window is defined as being from 0 to 10, the
 * chart will display the values between 0 and 10.
 * @property xWindow Visualization window on the x-axis.
 * @property yWindow Visualization window on the y-axis.
 */
public data class ChartWindow(
    val xWindow: ClosedFloatingPointRange<Float>,
    val yWindow: ClosedFloatingPointRange<Float>,
) {
    public companion object {
        /**
         * Creates a new ChartWindow based on the given dataset. The window will be defined as the
         * range of the dataset.
         */
        public fun fromDataset(dataset: Dataset): ChartWindow {
            return ChartWindow(
                xWindow = dataset.xRange,
                yWindow = dataset.yRange
            )
        }
    }
}
