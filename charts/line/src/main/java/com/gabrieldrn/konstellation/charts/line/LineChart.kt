package com.gabrieldrn.konstellation.charts.line

import android.graphics.Typeface
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.drawing.drawFrame
import com.gabrieldrn.konstellation.drawing.drawLines
import com.gabrieldrn.konstellation.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.drawing.drawZeroLines
import com.gabrieldrn.konstellation.drawing.highlightPoint
import com.gabrieldrn.konstellation.geometry.createOffsets
import com.gabrieldrn.konstellation.highlighting.BoxedPopup
import com.gabrieldrn.konstellation.highlighting.HighlightPopupScope
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.nearestPointByX
import com.gabrieldrn.konstellation.plotting.xMax
import com.gabrieldrn.konstellation.plotting.xMin
import com.gabrieldrn.konstellation.plotting.xRange
import com.gabrieldrn.konstellation.plotting.yMax
import com.gabrieldrn.konstellation.plotting.yMin
import com.gabrieldrn.konstellation.plotting.yRange
import com.gabrieldrn.konstellation.style.setColor
import com.gabrieldrn.konstellation.util.randomFancyDataSet

/**
 * Konstellation composable function drawing a line chart.
 * @param dataset Your set of points.
 * @param modifier Your classic Jetpack-Compose modifier.
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
    // TODO Move highlight positions in line chart properties
    highlightPositions: Set<HighlightPosition> = setOf(HighlightPosition.POINT),
    highlightContent: (@Composable HighlightPopupScope.(Point) -> Unit)? = null
) {
    Box {
        val hapticLocal = LocalHapticFeedback.current
        var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }

        // In order to force the Canvas pointerInput modifier to be re-composed, the dataset is
        // "moved" inside a state.
        var points by remember { mutableStateOf<Dataset>(listOf()) }
        points = dataset

        val xDrawRange = properties.dataXRange ?: dataset.xRange
        val yDrawRange = properties.dataYRange ?: dataset.yRange

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
        }

        highlightedValue?.let {
            ComposeHighlightPopup(highlightContent, highlightPositions, it, properties)
        }
    }
}

@Composable
private fun BoxScope.ComposeHighlightPopup(
    highlightContent: @Composable (HighlightPopupScope.(Point) -> Unit)?,
    highlightPositions: Set<HighlightPosition>,
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

/**
 * @suppress
 */
@ExperimentalComposeUiApi
@Preview
@Composable
fun LineChartPreview() {
    Box(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val dataset = randomFancyDataSet()
        LineChart(
            modifier = Modifier.fillMaxSize(),
            dataset = dataset,
            properties = LineChartProperties().apply {
                chartPaddingValues = PaddingValues(44.dp)
                dataXRange = (dataset.xMin - 1)..(dataset.xMax + 1)
                dataYRange = (dataset.yMin - 1)..(dataset.yMax + 1)
                axes = setOf(
                    Axes.xBottom.apply { style.setColor(Color.Black) },
                    Axes.yLeft.apply { style.setColor(Color.Black) },
                )
                setAxisTypeface(Typeface.MONOSPACE)
            }
        )
    }
}
