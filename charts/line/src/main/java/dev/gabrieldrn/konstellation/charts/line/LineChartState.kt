package dev.gabrieldrn.konstellation.charts.line

import androidx.annotation.VisibleForTesting
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
import dev.gabrieldrn.konstellation.util.zoomAround
import timber.log.Timber

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
     * Current gestures state to be applied to the chart window.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var gesturesState by mutableStateOf(GESTURES_STATE_INITIAL)

    /**
     * The current [ChartWindow] of the chart, taking into account [gesturesState] and [size]. Any
     * change in those values will trigger a recalculation of this value.
     */
    public val window: ChartWindow by derivedStateOf {
        val xPan = gesturesState.pan.x
            .takeIf { it != 0f }
            ?.map(0f..size.width, initialWindow.xWindow)
            ?.times(-1)
            ?.times(gesturesState.scale) // Keeps the panning consistent with the zoom state.
            ?: 0f
        val yPan = gesturesState.pan.y
            .takeIf { it != 0f }
            ?.map(0f..size.height, initialWindow.yWindow)
            // Referential is the middle of the chart, so it needs to be subtracted from the pan.
            ?.minus(yReferential)
            ?.times(gesturesState.scale)
            ?: 0f
        initialWindow.copy(
            xWindow = initialWindow.xWindow
                .plus(xPan)
                .zoomAround(gesturesState.scale),
            yWindow = initialWindow.yWindow
                .plus(yPan)
                .zoomAround(gesturesState.scale)
        )
    }

    /**
     * The dataset with the offsets applied.
     */
    public val calculatedDataset: Dataset by derivedStateOf {
        check(size != Size.Zero) {
            "Size must be set to calculate the dataset"
        }
        if (gesturesState === GESTURES_STATE_INITIAL) {
            yReferential = initialWindow.yWindow.distance
                .div(2f)
                .plus(initialWindow.yWindow.start)
            gesturesState = gesturesState.copy(
                pan = Offset(
                    x = initialWindow.xWindow.distance
                        .map(initialWindow.xWindow, size.width..0f)
                        .times(-1f),
                    y = yReferential
                        .map(initialWindow.yWindow, 0f..size.height)
                )
            )
        }
        dataset.createOffsets(
            size = size,
            xWindowRange = window.xWindow,
            yWindowRange = window.yWindow
        )
    }

    /**
     * Registers the new size of the canvas that draws the chart. A recalculation of the chart is
     * necessary after this, therefore the chart will be recomposed.
     */
    public fun updateSize(newSize: IntSize) {
        size = newSize.toSize()
        gesturesState = GESTURES_STATE_INITIAL
    }

    /**
     * Applies panning and zooming gestures to the chart.
     */
    public fun applyTransformGestures(pan: Offset = Offset.Zero, zoom: Float = 0f) {
        if (!gesturesEnabled) return
        gesturesState = gesturesState.copy(
            pan = gesturesState.pan + pan,
            scale = (gesturesState.scale + (zoom - 1) * -1).coerceIn(SCALING_MIN, SCALING_MAX)
        ).also {
            Timber.d("pan: $pan, zoom: $zoom, state: $it")
        }
    }

    /**
     * Holds the transformation gestures states applied to the chart.
     */
    @Immutable
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal data class TransformGesturesState(
        val pan: Offset = Offset.Zero,
        val scale: Float = 1f
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal companion object {
        // TODO Make these configurable
        private const val SCALING_MIN = 0.01f
        private const val SCALING_MAX = 2f

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        val GESTURES_STATE_INITIAL = TransformGesturesState()
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
