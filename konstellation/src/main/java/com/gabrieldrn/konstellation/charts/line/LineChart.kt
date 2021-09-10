package com.gabrieldrn.konstellation.charts.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalHapticFeedback
import com.gabrieldrn.konstellation.core.data.*
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.core.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.core.highlighting.BoxedPopup
import com.gabrieldrn.konstellation.core.highlighting.HighlightPopupScope
import com.gabrieldrn.konstellation.core.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.core.plotting.*
import kotlin.math.absoluteValue

/**
 * Konstellation composable function drawing a line chart.
 * @param dataset Your set of points.
 * @param modifier Your classic Jetpack-Compose modifier
 * @param properties The DNA of your chart. See [LineChartProperties].
 * @param highlightPositions Where to place highlighting popups. There will be as much popups as
 * there is positions.
 * @param highlightContent Classic Composable scope defining the content to be shown inside
 * highlighting popup(s).
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

    var xRange by remember { mutableStateOf(properties.dataXRange ?: dataset.xRange) }
    var yRange by remember { mutableStateOf(properties.dataYRange ?: dataset.yRange) }
    var panScalerX = 0f
    var panScalerY = 0f

    Box {
        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val z = 1 / zoom
                        val xOffset = -(pan.x * panScalerX)
                        val yOffset = pan.y * panScalerY
                        xRange = (xRange.start + xOffset) * z..(xRange.endInclusive + xOffset) * z
                        yRange = (yRange.start + yOffset) * z..(yRange.endInclusive + yOffset) * z
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            hapticLocal.performHapticFeedback(HapticFeedbackType.LongPress)
                            highlightedValue = points.nearestPointByX(it.x)
                        },
                        onDragEnd = {
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
                        highlightPoint(
                            it, highlightPositions, highlightPointStyle, highlightLineStyle
                        )
                    }
                }
                drawScaledAxis(this, xRange, yRange)
            }

            panScalerX = xRange.endInclusive - 1f.convertFromRanges(0f..size.width, xRange).absoluteValue
            panScalerY = yRange.endInclusive - 1f.convertFromRanges(0f..size.height, yRange).absoluteValue
        }

        highlightedValue?.let {
            ComposeHighlightPopup(highlightContent, highlightPositions, it, properties)
        }
    }
}

@Composable
private fun BoxScope.ComposeHighlightPopup(
    highlightContent: @Composable (HighlightPopupScope.(Point) -> Unit)?,
    highlightPositions: Array<HighlightPosition>,
    point: Point,
    properties: LineChartProperties
) {
    if (highlightContent != null) {
        highlightPositions.forEach { position ->
            BoxedPopup(
                HighlightPopupScope(
                    point, position, properties.chartPaddingValues
                ).apply { ComputePaddings() },
            ) { point ->
                highlightContent(point)
            }
        }
    }
}
