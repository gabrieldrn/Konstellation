package com.gabrieldrn.konstellation.charts.line.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.drawing.drawFrame
import com.gabrieldrn.konstellation.drawing.drawLines
import com.gabrieldrn.konstellation.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.drawing.drawZeroLines
import com.gabrieldrn.konstellation.drawing.highlightPoint
import com.gabrieldrn.konstellation.highlighting.BoxedPopup
import com.gabrieldrn.konstellation.highlighting.HighlightScope
import com.gabrieldrn.konstellation.math.createOffsets
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.plotting.nearestPointByX
import com.gabrieldrn.konstellation.plotting.xRange
import com.gabrieldrn.konstellation.plotting.yRange
import com.gabrieldrn.konstellation.util.applyDatasetOffsets
import com.gabrieldrn.konstellation.util.randomFancyDataSet

/**
 * Konstellation composable function drawing a line chart.
 * @param dataset Your set of points.
 * @param modifier Your classic Jetpack-Compose modifier.
 * @param properties The DNA of your chart. See [LineChartProperties].
 * @param highlightContent Classic Composable scope defining the content to be shown inside
 * highlight popup(s). This is optional.
 * @param onHighlightChange Callback invoked each time the highlighted value changes. This is
 * optional, and it's a light alternative to [highlightContent] to have feedback on highlighting
 * without having to draw content above the chart.
 */
@Composable
fun LineChart(
    dataset: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties(),
    styles: LineChartStyles = LineChartStyles(),
    highlightContent: (@Composable HighlightScope.() -> Unit)? = null,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
    val hapticLocal = LocalHapticFeedback.current
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }

    // In order to enable re-composition of the Canvas pointerInput modifier, the dataset is
    // "moved" inside a state.
    var points by remember { mutableStateOf(datasetOf()) }
    points = dataset

    val (xDrawRange, yDrawRange) = properties.datasetOffsets.applyDatasetOffsets(
        xDrawRange = dataset.xRange,
        yDrawRange = dataset.yRange
    )

    Box {
        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            hapticLocal.performHapticFeedback(HapticFeedbackType.LongPress)
                            highlightedValue = points.nearestPointByX(it.x)
                            onHighlightChange?.invoke(highlightedValue)
                        },
                        onDragEnd = {
                            highlightedValue = null
                            onHighlightChange?.invoke(highlightedValue)
                        },
                        onDrag = { change, _ ->
                            highlightedValue = points.nearestPointByX(change.position.x)
                            onHighlightChange?.invoke(highlightedValue)
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

            drawZeroLines(xDrawRange, yDrawRange)

            with(styles) {
                clipRect {
                    // Lines between data points
                    drawLines(points, lineStyle, pointStyle, properties.drawPoints)
                    // Highlight
                    highlightedValue?.let {
                        highlightPoint(
                            point = it,
                            contentPositions = properties.highlightContentPositions,
                            pointStyle = highlightPointStyle,
                            linePosition = properties.highlightLinePosition,
                            lineStyle = highlightLineStyle
                        )
                    }
                }
                drawScaledAxis(properties, styles, xDrawRange, yDrawRange)
            }
        }

        highlightedValue?.let { point ->
            ComposeHighlightPopup(highlightContent, point, properties)
        }
    }
}

@Composable
private fun BoxScope.ComposeHighlightPopup(
    highlightContent: @Composable (HighlightScope.() -> Unit)?,
    point: Point,
    properties: LineChartProperties
) {
    if (highlightContent != null) {
        properties.highlightContentPositions.forEach { position ->
            BoxedPopup(
                HighlightScope(
                    point, position, properties.chartPaddingValues
                ).apply { ComputePaddings() },
            ) {
                highlightContent()
            }
        }
    }
}

/**
 * @suppress
 */
@Preview(showBackground = true)
@Composable
fun LineChartPreview() {
    LineChart(
        dataset = randomFancyDataSet(),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        properties = LineChartProperties(
            datasetOffsets = DatasetOffsets(
                xStartOffset = 2f,
                xEndOffset = 2f,
                yStartOffset = 0.5f,
                yEndOffset = 0.5f
            )
        )
    )
}
