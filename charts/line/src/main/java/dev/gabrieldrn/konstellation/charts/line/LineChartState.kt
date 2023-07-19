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
import dev.gabrieldrn.konstellation.util.distance
import dev.gabrieldrn.konstellation.util.plus
import kotlin.jvm.Throws

/**
 * State of a line chart. This is the main class used by the composable function, and it stores
 * states needed to draw the chart (e.g. panning). This class is not meant to be used directly, but
 * rather to be created by the composable function. See [LineChart].
 *
 * @param dataset Your set of points.
 * @property properties The properties of the line chart. See [LineChartProperties].
 */
@Stable
public class LineChartState(
    dataset: Dataset,
    public val properties: LineChartProperties,
) {
    /**
     * The initial window of the chart, before any panning.
     */
    private val initialWindow = properties.chartWindow ?: ChartWindow.fromDataset(dataset)

    /**
     * Whether gestures are enabled.
     */
    private val gesturesEnabled = properties.gesturesEnabled

    /**
     * Canvas size.
     */
    private var size by mutableStateOf(Size.Zero)

    /**
     * The reference point for the y-axis where half of the chart size is half of the y-range
     * distance.
     */
    private var yReferential by mutableStateOf(0f)

    /**
     * The current panning state.
     */
    private var panningState by mutableStateOf(PANNING_STATE_ZERO)

    private var zoomFactorState by mutableStateOf(ZoomFactorState())

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
            // Referential is the middle of the chart, so it needs to be subtracted from the pan.
            ?.minus(yReferential)
            ?: 0f
        initialWindow.copy(
            xWindow = initialWindow.xWindow
                .plus(xPan)
                .zoomAround(zoomFactorState.x),
            yWindow = initialWindow.yWindow
                .plus(yPan)
                .zoomAround(zoomFactorState.y)
        )
    }

    /**
     * The dataset with the offsets applied.
     */
    public val calculatedDataset: Dataset by derivedStateOf {
        check(size != Size.Zero) {
            "Size must be set to calculate the dataset"
        }
        if (panningState === PANNING_STATE_ZERO) {
            yReferential = initialWindow.yWindow.distance
                .div(2f)
                .plus(initialWindow.yWindow.start)
            panningState = panningState.copy(
                x = initialWindow.xWindow.distance
                    .map(initialWindow.xWindow, size.width..0f)
                    .times(-1f),
                y = yReferential
                    .map(initialWindow.yWindow, 0f..size.height)
            )
        }
        dataset.createOffsets(
            size = size,
            xWindowRange = window.xWindow,
            yWindowRange = window.yWindow
        )
    }

    private fun ClosedFloatingPointRange<Float>.zoomAround(
        zoomFactor: Float
    ): ClosedFloatingPointRange<Float> {
        // FIXME / WIP - The zoom occurs around the middle of the chart (0, 0), but it should be
        //  around
        val newStart = start * zoomFactor
        val newEnd = endInclusive * zoomFactor
        return newStart..newEnd
    }

    /**
     * Registers the new size of the canvas that draws the chart. A recalculation of the chart is
     * necessary after this, therefore the chart will be recomposed.
     */
    public fun updateSize(newSize: IntSize) {
        size = newSize.toSize()
        zoomFactorState = ZoomFactorState()
    }

    /**
     * Pans the chart by the given amount.
     */
    public fun pan(amount: Offset) {
        if (!gesturesEnabled) return
        panningState = panningState.copy(
            x = panningState.x + amount.x,
            y = panningState.y + amount.y
        )
    }

    /**
     * Applies panning and zooming gestures to the chart.
     */
    public fun applyTransformGestures(pan: Offset, zoom: Float) {
        if (!gesturesEnabled) return
        zoomFactorState = ZoomFactorState(
            x = zoomFactorState.x + (zoom - 1) * -1f,
            y = zoomFactorState.y + (zoom - 1) * -1f
        )
        panningState = panningState.copy(
            x = panningState.x + pan.x,
            y = panningState.y + pan.y
        )
    }

    @Stable
    private data class ZoomFactorState(
        val x: Float = 1f,
        val y: Float = 1f
    )

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

    private companion object {
        private val PANNING_STATE_ZERO = PanningState(0f, 0f)
    }
}

/**
 * Remembers a [LineChartState] that will be recomposed whenever the given [dataset] or
 * [chartProperties] changes.
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
