package com.gabrieldrn.konstellation.charts.line

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import com.gabrieldrn.konstellation.core.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.core.geometry.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.geometry.convertCanvasYToDataY
import com.gabrieldrn.konstellation.core.geometry.createOffsets
import com.gabrieldrn.konstellation.core.highlighting.BoxedPopup
import com.gabrieldrn.konstellation.core.highlighting.HighlightPopupScope
import com.gabrieldrn.konstellation.core.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.core.plotting.*
import kotlin.math.abs

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
    Box {
        val hapticLocal = LocalHapticFeedback.current
        var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }

        // In order to force the Canvas pointerInput modifier to be re-composed, the dataset is
        // "moved" inside a state.
        var points by remember { mutableStateOf<Dataset>(listOf()) }
        points = dataset

        var xDrawRange by remember { mutableStateOf(properties.dataXRange ?: dataset.xRange) }
        var yDrawRange by remember { mutableStateOf(properties.dataYRange ?: dataset.yRange) }
        var panScaler = Offset(0f, 0f)

        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val z = 1 / zoom
                        val x = -pan.x * panScaler.x
                        val y = pan.y * panScaler.y
                        xDrawRange = (xDrawRange.start + x) * z..(xDrawRange.endInclusive + x) * z
                        yDrawRange = (yDrawRange.start + y) * z..(yDrawRange.endInclusive + y) * z
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
                dataSetXRange = xDrawRange,
                dataSetYRange = yDrawRange
            )

            with(properties) {
                drawZeroLines(xDrawRange, yDrawRange)
                clipRect(0f, 0f, size.width, size.height) {
                    drawLines(points, lineStyle, pointStyle, drawPoints = true)
                    highlightedValue?.let {
                        highlightPoint(
                            it, highlightPositions, highlightPointStyle, highlightLineStyle
                        )
                    }
                }
                drawScaledAxis(this, xDrawRange, yDrawRange)
            }

            panScaler = Offset(
                xDrawRange.endInclusive - abs(convertCanvasXToDataX(1f, xDrawRange)),
                yDrawRange.endInclusive - abs(convertCanvasYToDataY(1f, yDrawRange))
            )
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
