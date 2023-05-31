package dev.gabrieldrn.konstellation.charts.line

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.math.createOffsets
import dev.gabrieldrn.konstellation.math.map
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.validate
import dev.gabrieldrn.konstellation.util.rawRange
import kotlin.jvm.Throws

/**
 * State of a line chart. This is the main class used by the composable function, and it stores
 * states needed to draw the chart (e.g. panning). This class is not meant to be used directly, but
 * rather to be created by the composable function. See [LineChart].
 *
 * @param dataset Your set of points.
 * @param properties The properties of the line chart. See [LineChartProperties].
 */
@Stable
public class LineChartState(
    dataset: Dataset,
    properties: LineChartProperties,
) {
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
        check(size != Size.Zero) {
            "Size must be set to calculate the dataset"
        }
        if (!hasPanned) {
            panningState = panningState.copy(
                x = initialWindow.xWindow.rawRange
                    .map(initialWindow.xWindow, size.width..0f)
                    .times(-1f),
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
 *
 * @throws IllegalArgumentException If the dataset presents invalid values.
 */
@Composable
@Throws(IllegalArgumentException::class)
public fun rememberLineChartState(
    dataset: Dataset,
    chartProperties: LineChartProperties
): LineChartState = remember(
    dataset,
    chartProperties
) {
    dataset.validate()
    LineChartState(
        dataset = dataset,
        properties = chartProperties,
    )
}
