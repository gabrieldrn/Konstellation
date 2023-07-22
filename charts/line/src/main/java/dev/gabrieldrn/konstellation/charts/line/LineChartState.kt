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
import dev.gabrieldrn.konstellation.util.zoomAround
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
     * Current gestures state to be applied to the chart window.
     */
    private var gesturesState by mutableStateOf(GESTURES_STATE_INITIAL)

    /**
     * The current [ChartWindow] of the chart, taking into account [gesturesState] and [size].
     */
    public val window: ChartWindow by derivedStateOf {
        val xPan = gesturesState.panning.x
            .takeIf { it != 0f }
            ?.map(0f..size.width, initialWindow.xWindow)
            ?.times(-1)
            ?.times(gesturesState.zoomFactor) // Keeps the panning consistent with the zoom state.
            ?: 0f
        val yPan = gesturesState.panning.y
            .takeIf { it != 0f }
            ?.map(0f..size.height, initialWindow.yWindow)
            // Referential is the middle of the chart, so it needs to be subtracted from the pan.
            ?.minus(yReferential)
            ?.times(gesturesState.zoomFactor)
            ?: 0f
        initialWindow.copy(
            xWindow = initialWindow.xWindow
                .plus(xPan)
                .zoomAround(gesturesState.zoomFactor),
            yWindow = initialWindow.yWindow
                .plus(yPan)
                .zoomAround(gesturesState.zoomFactor)
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
                panning = Offset(
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
    public fun applyTransformGestures(pan: Offset, zoom: Float) {
        if (!gesturesEnabled) return
        gesturesState = gesturesState.copy(
            panning = gesturesState.panning + pan,
            zoomFactor = gesturesState.zoomFactor + (zoom - 1) * -1
        )
    }

    /**
     * The transformation gestures applied by the user to the chart.
     */
    @Stable
    private data class TransformGesturesState(
        val panning: Offset = Offset.Zero,
        val zoomFactor: Float = 1f
    )

    private companion object {
        private val GESTURES_STATE_INITIAL = TransformGesturesState()
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
