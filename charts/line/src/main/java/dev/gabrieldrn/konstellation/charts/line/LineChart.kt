package dev.gabrieldrn.konstellation.charts.line

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
import dev.gabrieldrn.konstellation.charts.line.drawing.drawLinePath
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartHighlightConfig
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellation.drawing.drawFrame
import dev.gabrieldrn.konstellation.drawing.drawPoint
import dev.gabrieldrn.konstellation.drawing.drawScaledAxis
import dev.gabrieldrn.konstellation.drawing.drawZeroLines
import dev.gabrieldrn.konstellation.drawing.highlightPoint
import dev.gabrieldrn.konstellation.highlighting.HighlightBox
import dev.gabrieldrn.konstellation.highlighting.HighlightScope
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
 *
 * @param dataset Your set of points.
 * @param modifier The modifier to be applied to the chart.
 * @param properties The DNA of your chart. See [LineChartProperties].
 * @param styles Visual styles to be applied to the chart.
 * @param highlightConfig Configuration of the highlight feature. See [LineChartHighlightConfig].
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
    highlightConfig: LineChartHighlightConfig = LineChartHighlightConfig(),
    highlightContent: (@Composable HighlightScope.() -> Unit)? = null,
    onHighlightChange: ((Point?) -> Unit)? = null
) {
    LineChart(
        state = rememberLineChartState(dataset, properties),
        modifier = modifier,
        styles = styles,
        highlightConfig = highlightConfig,
        highlightContent = highlightContent,
        onHighlightChange = onHighlightChange
    )
}

/**
 * Konstellation composable function drawing a line chart.
 *
 * @param state The state of the chart. See [LineChartState]. A change in the state will trigger
 * a recomposition of the chart.
 * @param modifier The modifier to be applied to the chart.
 * @param styles Visual styles to be applied to the chart.
 * @param highlightConfig Configuration of the highlight feature. See [LineChartHighlightConfig].
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
    highlightConfig: LineChartHighlightConfig = LineChartHighlightConfig(),
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
            if (styles.drawFrame) {
                drawFrame()
            }

            val path = styles.pathInterpolator(state.calculatedDataset)

            if (styles.drawZeroLines) {
                drawZeroLines(state.window.xWindow, state.window.yWindow)
            }

            with(styles) {
                drawScaledAxis(
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
                            lineTo(state.calculatedDataset.last().xPos, size.height)
                            lineTo(state.calculatedDataset.first().xPos, size.height)
                            close()
                        },
                        brush = brush
                    )
                }

                if (styles.drawLines) {
                    // Chart line path
                    drawLinePath(
                        path,
                        lineStyle
                    )
                }

                // Points
                if (styles.drawPoints) {
                    state.calculatedDataset.forEach { drawPoint(it, pointStyle) }
                }
            }
        }

        key(state) {
            HighlightCanvas(
                modifier = modifier,
                properties = state.properties,
                dataset = { state.calculatedDataset },
                highlightConfig = highlightConfig,
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
    highlightConfig: LineChartHighlightConfig,
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
            if (properties.panningEnabled) {
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
                contentPositions = highlightConfig.contentPositions,
                pointStyle = highlightConfig.pointStyle,
                linesPlacement = highlightConfig.linesPlacement,
                lineStyle = highlightConfig.lineStyle
            )
        }
    }

    highlightedPoint?.let { point ->
        if (highlightContent != null) {
            highlightConfig.contentPositions.forEach { position ->
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
        properties = LineChartProperties(),
        styles = LineChartStyles()
    )
}
