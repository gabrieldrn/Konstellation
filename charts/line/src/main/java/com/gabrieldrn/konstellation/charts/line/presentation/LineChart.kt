package com.gabrieldrn.konstellation.charts.line.presentation

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
import com.gabrieldrn.konstellation.charts.line.drawing.drawLinePath
import com.gabrieldrn.konstellation.charts.line.drawing.toLinePath
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.drawing.drawFrame
import com.gabrieldrn.konstellation.drawing.drawPoint
import com.gabrieldrn.konstellation.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.drawing.drawZeroLines
import com.gabrieldrn.konstellation.drawing.highlightPoint
import com.gabrieldrn.konstellation.highlighting.BoxedPopup
import com.gabrieldrn.konstellation.highlighting.HighlightScope
import com.gabrieldrn.konstellation.math.createOffsets
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
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
 * @param styles Visual styles to be applied to the chart. See [LineChartStyles].
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
    val (xDrawRange, yDrawRange) = properties.datasetOffsets.applyDatasetOffsets(
        xDrawRange = dataset.xRange,
        yDrawRange = dataset.yRange
    )

    Box {
        Canvas(modifier.padding(properties.chartPaddingValues)) {
            if (properties.drawFrame) {
                drawFrame()
            }

            dataset.createOffsets(
                drawScope = this,
                dataSetXRange = xDrawRange,
                dataSetYRange = yDrawRange
            )

            if (properties.drawZeroLines) {
                drawZeroLines(xDrawRange, yDrawRange)
            }

            with(styles) {
                drawScaledAxis(properties, styles, xDrawRange, yDrawRange)

                clipRect {

                    // Background filling
                    properties.fillingBrush?.let { brush ->
                        drawPath(
                            path = dataset.toLinePath(properties.smoothing).apply {
                                // Closing path shape with chart bottom
                                lineTo(dataset.last().xPos, size.height)
                                lineTo(dataset[0].xPos, size.height)
                                close()
                            },
                            brush = brush
                        )
                    }

                    if (properties.drawLines) {
                        // Lines between data points
                        drawLinePath(
                            dataset,
                            properties.smoothing,
                            lineStyle
                        )
                    }

                    // Points
                    if (properties.drawPoints) {
                        dataset.forEach { drawPoint(it, pointStyle) }
                    }
                }
            }
        }

        HighlightCanvas(
            modifier = modifier,
            properties = properties,
            dataset = dataset,
            styles = styles,
            highlightContent = highlightContent,
            onHighlightChange = onHighlightChange
        )
    }
}

@Composable
private fun BoxScope.HighlightCanvas(
    modifier: Modifier,
    properties: LineChartProperties,
    dataset: Dataset,
    styles: LineChartStyles,
    highlightContent: @Composable (HighlightScope.() -> Unit)?,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
    val hapticLocal = LocalHapticFeedback.current
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }

    LaunchedEffect(highlightedValue) {
        if (properties.hapticHighlight && highlightedValue != null) {
            hapticLocal.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        onHighlightChange?.invoke(highlightedValue)
    }

    Canvas(
        modifier = modifier
            .padding(properties.chartPaddingValues)
            .pointerInput(dataset) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        highlightedValue = dataset.nearestPointByX(it.x)
                    },
                    onDragEnd = {
                        highlightedValue = null
                    },
                    onDrag = { change, _ ->
                        highlightedValue = dataset.nearestPointByX(change.position.x)
                    }
                )
            }
    ) {
        // Highlight
        highlightedValue?.let {
            highlightPoint(
                point = it,
                contentPositions = properties.highlightContentPositions,
                pointStyle = styles.highlightPointStyle,
                linePosition = properties.highlightLinePosition,
                lineStyle = styles.highlightLineStyle
            )
        }
    }

    highlightedValue?.let { point ->
        ComposeHighlightPopup(highlightContent, point, properties)
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

@Preview(showBackground = true)
@Composable
private fun LineChartPreview() {
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
