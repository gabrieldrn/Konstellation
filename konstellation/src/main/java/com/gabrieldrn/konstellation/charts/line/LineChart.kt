package com.gabrieldrn.konstellation.charts.line

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.core.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.core.highlighting.BoxedHighlightPopup
import com.gabrieldrn.konstellation.core.highlighting.HighlightPopupScope
import com.gabrieldrn.konstellation.core.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.core.plotting.*

/**
 * Composable responsible of plotting lines from a dataset and draw axis.
 */
@ExperimentalComposeUiApi
@Composable
fun LineChart(
    dataset: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties(),
    highlightPositions: Array<HighlightPosition> = arrayOf(HighlightPosition.POINT),
    highlightContent: (@Composable HighlightPopupScope.(Point) -> Unit)? = null
) {
    val hapticLocal = LocalHapticFeedback.current
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }

    // In order to force the Canvas pointerInput modifier to be re-composed, the dataset is "moved"
    // inside a state.
    var points by remember { mutableStateOf<Dataset>(listOf()) }
    points = dataset

    val xRange = properties.dataXRange ?: dataset.xRange
    val yRange = properties.dataYRange ?: dataset.yRange

    Box {
        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            hapticLocal.performHapticFeedback(HapticFeedbackType.LongPress)
                            highlightedValue = points.nearestPointByX(it.x)
                        },
                        onDragEnd = {
                            Log.d("LINE CHART", "DRAG ENDED")
                            highlightedValue = null
                        },
                        onDrag = { change, _ ->
                            highlightedValue = points.nearestPointByX(change.position.x)
                        }
                    )
                }
        ) {
            drawFrame()

            points.createOffsets(
                drawScope = this,
                dataSetXRange = xRange,
                dataSetYRange = yRange
            )

            with(properties) {
                drawZeroLines(xRange, yRange)
                clipRect(0f, 0f, size.width, size.height) {
                    drawLines(points, lineStyle, pointStyle, drawPoints = true)
                    highlightedValue?.let {
                        highlightPoint(it, highlightPositions, highlightPointStyle)
                    }
                }
                drawScaledAxis(this, points)
            }
        }

        highlightedValue?.let { point ->
            if (highlightContent != null) {
                highlightPositions.forEach { position ->
                    BoxedHighlightPopup(
                        HighlightPopupScope(
                            point, position, properties.chartPaddingValues
                        ).apply { ComputePaddings() }
                    ) { point ->
                        highlightContent(point)
                    }
                }
            }
        }
    }
}
