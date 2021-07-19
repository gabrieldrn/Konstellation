package com.gabrieldrn.konstellation.core.plotting

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

/**
 * Lambda invoked when a point needs to be highlighted.
 */
val highlight: DrawScope.(Point, PointDrawStyle, TextDrawStyle) -> Unit = DrawScope::highlightPoint

/**
 * Composable responsible of plotting lines from a dataset and draw axis.
 */
@ExperimentalComposeUiApi
@Composable
fun LinePlotter(
    dataSet: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties()
) {
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }
    highlightedValue = null
    val pointerListener: (MotionEvent) -> Boolean = {
        when (it.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                highlightedValue = dataSet.nearestPointByX(it.x)
                true
            }
            else -> false
        }
    }

    val xRange = properties.dataXRange ?: dataSet.xRange
    val yRange = properties.dataYRange ?: dataSet.yRange

    Canvas(
        modifier
            .padding(36.dp)
            .fillMaxSize()
            .pointerInteropFilter(null, pointerListener)
    ) {
        drawFrame()
        with(properties) {
            dataSet.createOffsets(
                drawScope = this@Canvas,
                dataSetXRange = xRange,
                dataSetYRange = yRange
            )
            drawZeroLines(xRange, yRange)
            drawLines(dataSet, lineStyle, pointStyle, drawPoints = true)
            highlightedValue?.let {
                highlight(this@Canvas, it, highlightPointStyle, highlightTextStyle)
            }
            drawScaledAxis(this, dataSet)
        }
    }
}

/**
 * Composable responsible of plotting points from a function and draw axis.
 */
@Composable
fun FunctionPlotter(
    modifier: Modifier = Modifier,
    lineStyle: LineDrawStyle = LineDrawStyle(),
    pointStyle: PointDrawStyle = PointDrawStyle(),
    textStyle: TextDrawStyle = TextDrawStyle(),
    pointSpacing: Int = 1,
    dataXRange: ClosedFloatingPointRange<Float>,
    dataYRange: ClosedFloatingPointRange<Float>,
    function: (x: Float) -> Float
) {
    Canvas(
        modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        val points = mutableListOf<Point>()
        fun addPoint(x: Float, y: Float) = points.add(x by y)
        (0..size.width.toInt() step pointSpacing.coerceAtLeast(1)).forEach {
            addPoint(it.toFloat(), function(convertCanvasXToDataX(it, dataXRange)))
        }
        addPoint(size.width, function(dataXRange.endInclusive))

        points.createOffsets(this, dataSetYRange = dataYRange)

        drawFrame()
        drawZeroLines(dataXRange, dataYRange)
        drawLines(points, lineStyle, pointStyle, false)
        drawMinMaxAxisValues(
            dataXRange.start,
            dataXRange.endInclusive,
            dataYRange.start,
            dataYRange.endInclusive,
            textStyle
        )
    }
}
