package com.gabrieldrn.konstellation.charts.line.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.drawing.drawLinePath
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.drawing.drawFrame
import com.gabrieldrn.konstellation.drawing.drawPoint
import com.gabrieldrn.konstellation.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.drawing.drawZeroLines
import com.gabrieldrn.konstellation.drawing.highlightPoint
import com.gabrieldrn.konstellation.highlighting.HighlightBox
import com.gabrieldrn.konstellation.highlighting.HighlightScope
import com.gabrieldrn.konstellation.math.createOffsets
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.plotting.nearestPointByX
import com.gabrieldrn.konstellation.plotting.xRange
import com.gabrieldrn.konstellation.plotting.yRange
import com.gabrieldrn.konstellation.util.applyDatasetOffsets

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
public fun LineChart(
    dataset: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties(),
    styles: LineChartStyles = LineChartStyles(),
    highlightContent: (@Composable HighlightScope.() -> Unit)? = null,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
    val (xWindowRange, yWindowRange) = properties.datasetOffsets.applyDatasetOffsets(
        xDrawRange = dataset.xRange,
        yDrawRange = dataset.yRange
    )

    var computedDataset by rememberSaveable { mutableStateOf(dataset) }

    // This layout helps to compute the offsets for the dataset during the first layout pass.
    Box {
        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val size = Size(
                        constraints.maxWidth.toFloat(),
                        constraints.maxHeight.toFloat()
                    )
                    computedDataset = dataset.createOffsets(
                        size = size,
                        xWindowRange = xWindowRange,
                        yWindowRange = yWindowRange
                    )
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeable.place(0, 0)
                    }
                }
        ) {
            if (properties.drawFrame) {
                drawFrame()
            }

            val path = properties.pathInterpolator(computedDataset)

            if (properties.drawZeroLines) {
                drawZeroLines(xWindowRange, yWindowRange)
            }

            with(styles) {
                drawScaledAxis(properties, styles, xWindowRange, yWindowRange)
                // Background filling
                properties.fillingBrush?.let { brush ->
                    drawPath(
                        path = Path().apply {
                            addPath(path)
                            // Closing path shape with chart bottom
                            lineTo(computedDataset.last().xPos, size.height)
                            lineTo(computedDataset.first().xPos, size.height)
                            close()
                        },
                        brush = brush
                    )
                }

                if (properties.drawLines) {
                    // Chart line path
                    drawLinePath(
                        path,
                        lineStyle
                    )
                }

                // Points
                if (properties.drawPoints) {
                    computedDataset.forEach { drawPoint(it, pointStyle) }
                }
            }
        }

        HighlightCanvas(
            modifier = modifier,
            properties = properties,
            dataset = { computedDataset },
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
    dataset: () -> Dataset,
    styles: LineChartStyles,
    highlightContent: @Composable (HighlightScope.() -> Unit)?,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
//    val hapticLocal = LocalHapticFeedback.current
    val density = LocalDensity.current

    var pointerValue by rememberSaveable { mutableStateOf<Float?>(null) }
    val highlightedPoint by remember {
        derivedStateOf {
            pointerValue?.let { dataset().nearestPointByX(it) }
        }
    }

    val chartTopPaddingPx = with(density) {
        properties.chartPaddingValues.calculateTopPadding().toPx().toInt()
    }

    val chartStartPaddingPx = with(density) {
        properties.chartPaddingValues.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
    }

    LaunchedEffect(highlightedPoint) {
        // FIXME The haptic feedback is called on each frame while the chart properties are
        //   being changed + when a point is highlighted.
//        if (properties.hapticHighlight && highlightedPoint != null) {
//            hapticLocal.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//        }
        onHighlightChange?.invoke(highlightedPoint)
    }

    Canvas(
        modifier = modifier
            .padding(properties.chartPaddingValues)
            .pointerInput(dataset) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { pointerValue = it.x },
                    onDragEnd = { pointerValue = null },
                    onDrag = { change, _ -> pointerValue = change.position.x }
                )
            }
    ) {
        // Highlight
        highlightedPoint?.let {
            highlightPoint(
                point = it,
                contentPositions = properties.highlightContentPositions,
                pointStyle = styles.highlightPointStyle,
                linePosition = properties.highlightLinePosition,
                lineStyle = styles.highlightLineStyle
            )
        }
    }

    highlightedPoint?.let { point ->
        if (highlightContent != null) {
            properties.highlightContentPositions.forEach { position ->
                HighlightBox(
                    scope = HighlightScope(point, position),
                    chartTopPaddingPx = chartTopPaddingPx,
                    chartStartPaddingPx = chartStartPaddingPx
                ) {
                    highlightContent()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LineChartPreview() {
    LineChart(
        dataset = datasetOf(
            0f by 0f,
            1f by 1f,
            2f by 1f,
            3f by 3f,
            4f by 3f,
            5f by 2f,
            6f by 2f,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        properties = LineChartProperties(
            axes = setOf(Axes.xBottom, Axes.yLeft),
            chartPaddingValues = PaddingValues(40.dp),
            datasetOffsets = DatasetOffsets(
                yStartOffset = 3f,
                yEndOffset = 3f
            )
        )
    )
}
