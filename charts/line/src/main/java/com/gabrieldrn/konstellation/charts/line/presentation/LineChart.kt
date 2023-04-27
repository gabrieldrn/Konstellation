package com.gabrieldrn.konstellation.charts.line.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.gabrieldrn.konstellation.charts.line.configuration.ChartWindow
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.drawing.drawLinePath
import com.gabrieldrn.konstellation.drawing.drawFrame
import com.gabrieldrn.konstellation.drawing.drawPoint
import com.gabrieldrn.konstellation.drawing.drawScaledAxis
import com.gabrieldrn.konstellation.drawing.drawZeroLines
import com.gabrieldrn.konstellation.drawing.highlightPoint
import com.gabrieldrn.konstellation.highlighting.HighlightBox
import com.gabrieldrn.konstellation.highlighting.HighlightScope
import com.gabrieldrn.konstellation.math.createOffsets
import com.gabrieldrn.konstellation.math.map
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.plotting.nearestPointByX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
@Suppress("CognitiveComplexMethod") // FIXME Try to simplify
@Composable
public fun LineChart(
    dataset: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties(),
    styles: LineChartStyles = LineChartStyles(),
    highlightContent: (@Composable HighlightScope.() -> Unit)? = null,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
    var size by remember { mutableStateOf(Size.Zero) }
    var panningState by remember { mutableStateOf(PanningState(0f, 0f)) }

    val initWindow = properties.chartWindow ?: ChartWindow.fromDataset(dataset)

    val window by remember(dataset, properties) {
        // This is the window that will be used to compute the offsets for the dataset, and to draw
        // the chart axes.
        // It's computed by always taking the initial window and applying the panning amount.
        derivedStateOf {
            val xPan = panningState.x
                .takeIf { it != 0f }
                ?.map(0f..size.width, initWindow.xWindow)
                ?.times(-1)
                ?: 0f
            val yPan = panningState.y
                .takeIf { it != 0f }
                ?.map(0f..size.height, initWindow.yWindow)
                ?: 0f
            initWindow.copy(
                xWindow = initWindow.xWindow.start + xPan..initWindow.xWindow.endInclusive + xPan,
                yWindow = initWindow.yWindow.start + yPan..initWindow.yWindow.endInclusive + yPan
            )
        }
    }

    val computedDataset by remember(dataset) {
        derivedStateOf {
            dataset.createOffsets(
                size = size,
                xWindowRange = window.xWindow,
                yWindowRange = window.yWindow
            ).also {
                if (!panningState.hasPanned) {
                    panningState = PanningState(
                        x = size.width / 2,
                        y = size.height / 2
                    )
                }
            }
        }
    }

    Box {
        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
                // This helps to compute the offsets for the dataset during the first layout pass.
                .onSizeChanged { size = it.toSize() }
        ) {
            if (properties.drawFrame) {
                drawFrame()
            }

            val path = properties.pathInterpolator(computedDataset)

            if (properties.drawZeroLines) {
                drawZeroLines(window.xWindow, window.yWindow)
            }

            with(styles) {
                drawScaledAxis(properties, styles, window.xWindow, window.yWindow)
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
            onHighlightChange = onHighlightChange,
            onUpdateWindowOffsets = { dragAmount ->
                panningState = panningState.copy(
                    x = panningState.x + dragAmount.x,
                    y = panningState.y + dragAmount.y,
                    hasPanned = true
                )
            }
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
    onHighlightChange: ((Point?) -> Unit)? = null,
    onUpdateWindowOffsets: ((dragAmount: Offset) -> Unit)
) {
//    val hapticLocal = LocalHapticFeedback.current
    val density = LocalDensity.current

    var pointerValue by remember {
        mutableStateOf<Float?>(null)
    }
    val highlightedPoint by remember(dataset) {
        derivedStateOf { pointerValue?.let { dataset().nearestPointByX(it) } }
    }

    val chartTopPaddingPx = with(density) {
        properties.chartPaddingValues.calculateTopPadding().toPx().toInt()
    }

    val chartStartPaddingPx = with(density) {
        properties.chartPaddingValues.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
    }

    val gesturesPointerInputScope: suspend PointerInputScope.() -> Unit = {
        withContext(Dispatchers.Main) {
            launch {
                // Highlighting
                detectDragGesturesAfterLongPress(
                    onDragStart = { pointerValue = it.x },
                    onDragEnd = { pointerValue = null },
                    onDrag = { change, _ -> pointerValue = change.position.x }
                )
            }
            if (properties.enablePanning) {
                launch {
                    // Panning
                    detectDragGestures { _, dragAmount ->
                        onUpdateWindowOffsets(dragAmount)
                    }
                }
            }
        }
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
            .pointerInput(Unit, gesturesPointerInputScope)
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

/**
 * State to keep track of the panning offset.
 * @property x The x offset.
 * @property y The y offset.
 * @property hasPanned Whether the chart has been panned. When false, this means that the chart has
 * not been panned yet and the initial panning offset should be set to the center of the chart.
 */
private data class PanningState(
    val x: Float,
    val y: Float,
    val hasPanned: Boolean = false
)

@Preview
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
            chartWindow = ChartWindow(
                xWindow = -1f..7f,
                yWindow = -3f..6f
            ),
        )
    )
}
