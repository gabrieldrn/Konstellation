package dev.gabrieldrn.konstellation.charts.line

import androidx.compose.ui.geometry.*
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.math.createOffsets
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LineChartStateTest {

    private val dataset = datasetOf(
        0f by 0f,
        1f by 1f,
        2f by 1f,
        3f by 3f,
        4f by 3f,
        5f by 2f,
        6f by 2f,
    )

    private val properties = LineChartProperties()

    private val canvasSize = IntSize(512, 512)

    private lateinit var state: LineChartState

    @Before
    fun setupState() {
        state = LineChartState(dataset, properties).apply {
            updateSize(canvasSize)
        }
    }

    @Test
    fun lineChartState_init_initializesCorrectly() {
        // After initialization, the window exposed by the state should be the same as the one
        // from the properties. The calculated dataset should have offsets calculated based on
        // the exposed window and the canvas size.

        // Window
        assertEquals(
            expected = ChartWindow.fromDataset(dataset),
            actual = state.window
        )

        // Dataset offsets calculation
        ChartWindow.fromDataset(dataset)
            .let {
                dataset.createOffsets(
                    size = canvasSize.toSize(),
                    xWindowRange = it.xWindow,
                    yWindowRange = it.yWindow,
                )
            }
            .map { it.offset }
            .zip(state.calculatedDataset.map { it.offset })
            .forEachIndexed { i, (expected, actual) ->
                assertEquals(
                    expected = expected,
                    actual = actual,
                    message = "Offset at index $i"
                )
            }
    }

    @Test
    fun lineChartState_updateSize_reInitializesTransformationGestures() {
        // When the size of the chart changes, the transformation gestures should be re-initialized
        // to the initial state, and the window state should be back to the initial one.
        state.applyTransformGestures(pan = Offset(1f, 1f))

        state.updateSize(IntSize(1024, 1024))

        assertEquals(
            expected = LineChartState.GESTURES_STATE_INITIAL,
            actual = state.gesturesState
        )
        assertEquals(
            expected = ChartWindow.fromDataset(dataset),
            actual = state.window
        )
    }
}
