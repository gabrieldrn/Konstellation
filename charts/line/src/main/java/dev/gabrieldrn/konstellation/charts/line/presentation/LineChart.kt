package dev.gabrieldrn.konstellation.charts.line.presentation

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
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellation.charts.line.drawing.drawLinePath
import dev.gabrieldrn.konstellation.drawing.drawFrame
import dev.gabrieldrn.konstellation.drawing.drawPoint
import dev.gabrieldrn.konstellation.drawing.drawScaledAxis
import dev.gabrieldrn.konstellation.drawing.drawZeroLines
import dev.gabrieldrn.konstellation.drawing.highlightPoint
import dev.gabrieldrn.konstellation.highlighting.HighlightBox
import dev.gabrieldrn.konstellation.highlighting.HighlightScope
import dev.gabrieldrn.konstellation.plotting.Axes
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.Point
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import dev.gabrieldrn.konstellation.plotting.nearestPointByX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Konstellation composable function drawing a line chart.
 * @param dataset Your set of points.
 * @param modifier The modifier to be applied to the chart.
 * @param properties The DNA of your chart. See [LineChartProperties].
 * @param styles Visual styles to be applied to the chart. Changing the styles will not trigger a
 * recomposition of the chart.
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
    LineChart(
        state = rememberLineChartState(dataset, properties),
        modifier = modifier,
        styles = styles,
        highlightContent = highlightContent,
        onHighlightChange = onHighlightChange
    )
}

/**
 * Konstellation composable function drawing a line chart.
 * @param state The state of the chart. See [LineChartState].
 * @param modifier The modifier to be applied to the chart.
 * @param styles Visual styles to be applied to the chart. Changing the styles will not trigger a
 * recomposition of the chart.
 * @param highlightContent Classic Composable scope defining the content to be shown inside
 * highlight popup(s). This is optional.
 * @param onHighlightChange Callback invoked each time the highlighted value changes. This is
 * optional, and it's a light alternative to [highlightContent] to have feedback on highlighting
 * without having to draw content above the chart.
 */
@Composable
public fun LineChart(
    state: LineChartState,
    modifier: Modifier = Modifier,
    styles: LineChartStyles = LineChartStyles(),
    highlightContent: (@Composable HighlightScope.() -> Unit)? = null,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
    // This layout helps to compute the offsets for the dataset during the first layout pass.
    Box {
        Canvas(
            modifier
                .padding(state.properties.chartPaddingValues)
                .onSizeChanged(state::updateSize)
        ) {
            if (state.properties.drawFrame) {
                drawFrame()
            }

            val path = state.properties.pathInterpolator(state.computedDataset)

            if (state.properties.drawZeroLines) {
                drawZeroLines(state.window.xWindow, state.window.yWindow)
            }

            with(styles) {
                drawScaledAxis(
                    state.properties,
                    styles,
                    state.window.xWindow,
                    state.window.yWindow
                )
                // Background filling
                styles.fillingBrush?.let { brush ->
                    drawPath(
                        path = Path().apply {
                            addPath(path)
                            // Closing path shape with chart bottom
                            lineTo(state.computedDataset.last().xPos, size.height)
                            lineTo(state.computedDataset.first().xPos, size.height)
                            close()
                        },
                        brush = brush
                    )
                }

                if (state.properties.drawLines) {
                    // Chart line path
                    drawLinePath(
                        path,
                        lineStyle
                    )
                }

                // Points
                if (state.properties.drawPoints) {
                    state.computedDataset.forEach { drawPoint(it, pointStyle) }
                }
            }
        }

        key(state) {
            HighlightCanvas(
                modifier = modifier,
                properties = state.properties,
                dataset = { state.computedDataset },
                styles = styles,
                highlightContent = highlightContent,
                onHighlightChange = onHighlightChange,
                onUpdateWindowOffsets = state::pan
            )
        }
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
