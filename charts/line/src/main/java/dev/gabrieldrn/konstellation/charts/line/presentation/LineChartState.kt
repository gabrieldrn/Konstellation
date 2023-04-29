package dev.gabrieldrn.konstellation.charts.line.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.math.createOffsets
import dev.gabrieldrn.konstellation.math.map
import dev.gabrieldrn.konstellation.plotting.Dataset

/**
 * State of a line chart. This is the main class used by the composable function, and it contains
 * all the data needed to draw the chart. It maintains rendering optimizations, such as caching
 * the drawing process by only redrawing when needed, and doing recomposions only when necessary.
 * It is also used to store the state of the chart (e.g. panning). This class is not meant to be
 * used directly, but rather to be created by the composable function. See [LineChart].
 *
 * The dataset is validated upon creation, and an [IllegalArgumentException] is thrown if it is
 * invalid. The validation checks for:
 *  - Non-emptiness
 *  - Monotonicity on the x-axis (xi < xi+1)
 *  - Duplicate values on the x-axis
 *  - NaN values
 *  - Infinite values
 *
 * @property dataset Your set of points.
 * @property properties The properties of the line chart. See [LineChartProperties].
 * @throws IllegalArgumentException If the dataset presents invalid values.
 */
@Stable
public class LineChartState(
    public val dataset: Dataset,
    public val properties: LineChartProperties,
) {
    init {
        with(dataset) {
            require(isNotEmpty()) { "Dataset must not be empty" }
            // Check for duplicates on the x-axis
            require(map { it.x }.distinct().size == size) {
                "Dataset must not have duplicate values on the x-axis"
            }
            // Check monotonicity
            require(zipWithNext().all { (a, b) -> a.x < b.x }) {
                "Dataset must be monotonically increasing on the x-axis"
            }
            // Check for NaNs
            require(all { !it.x.isNaN() && !it.y.isNaN() }) {
                "Dataset must not have NaN values"
            }
            // Check for infinite values
            require(all { !it.x.isInfinite() && !it.y.isInfinite() }) {
                "Dataset must not have infinite values"
            }
        }
    }

    private val initialWindow = properties.chartWindow ?: ChartWindow.fromDataset(dataset)
    private var size by mutableStateOf(Size.Zero)
    private var panningState by mutableStateOf(PanningState(0f, 0f))
    private var hasPanned = false

    /**
     * The current [ChartWindow] of the chart, taking into account the panning state.
     */
    public val window: ChartWindow by derivedStateOf {
        val xPan = panningState.x
            .takeIf { it != 0f }
            ?.map(0f..size.width, initialWindow.xWindow)
            ?.times(-1)
            ?: 0f
        val yPan = panningState.y
            .takeIf { it != 0f }
            ?.map(0f..size.height, initialWindow.yWindow)
            ?: 0f
        initialWindow.copy(
            xWindow = initialWindow.xWindow.start + xPan..initialWindow.xWindow.endInclusive + xPan,
            yWindow = initialWindow.yWindow.start + yPan..initialWindow.yWindow.endInclusive + yPan
        )
    }

    /**
     * The dataset with the offsets applied.
     */
    public val calculatedDataset: Dataset by derivedStateOf {
        if (!hasPanned) {
            panningState = panningState.copy(
                x = size.width / 2f,
                y = size.height / 2f
            )
        }
        dataset.createOffsets(
            size = size,
            xWindowRange = window.xWindow,
            yWindowRange = window.yWindow
        )
    }

    /**
     * Registers the new size of the canvas that draws the chart. The chart offsets must be
     * recomputed when the size changes.
     */
    public fun updateSize(newSize: IntSize) {
        size = newSize.toSize()
    }

    /**
     * Pans the chart by the given amount.
     */
    public fun pan(amount: Offset) {
        hasPanned = true
        panningState = panningState.copy(
            x = panningState.x + amount.x,
            y = panningState.y + amount.y
        )
    }

    /**
     * State to keep track of the panning offset.
     * @property x The x offset.
     * @property y The y offset.
     */
    @Stable
    private data class PanningState(
        val x: Float,
        val y: Float,
    )
}

/**
 * Remembers a [LineChartState] that will be recomposed whenever the given [dataset] or
 * [chartProperties] change.
 */
@Composable
public fun rememberLineChartState(
    dataset: Dataset,
    chartProperties: LineChartProperties
): LineChartState = remember(
    dataset,
    chartProperties
) {
    LineChartState(
        dataset = dataset,
        properties = chartProperties,
    )
}
