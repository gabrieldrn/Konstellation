package com.gabrieldrn.konstellation.core.plotting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

/**
 * Composable responsible of plotting lines from a dataset and draw axis.
 */
@Composable
fun LinePlotter(
    dataSet: Collection<Point>,
    modifier: Modifier = Modifier,
    chartName: String = "",
    lineStyle: LineDrawStyle,
    textStyle: TextDrawStyle,
) {
    Canvas(
        modifier
            .padding(16.dp)
            .padding(top = if (chartName.isNotEmpty()) 16.dp else 0.dp)
            .fillMaxSize()
    ) {
        dataSet.createOffsets(this)
        drawChartTitle(chartName, textStyle)
        drawFrame()
        drawZeroLines(dataSet.xRange, dataSet.yRange)
        clipRect(
            -1.dp.toPx(),
            -1.dp.toPx(),
            size.width + 1.dp.toPx(),
            size.height + 1.dp.toPx()
        ) {
            drawLines(dataSet, lineStyle)
        }
        drawMinMaxAxisValues(dataSet, textStyle)
    }
}

/**
 * Composable responsible of plotting points from a function and draw axis.
 */
@Composable
fun FunctionPlotter(
    modifier: Modifier = Modifier,
    chartName: String = "",
    lineStyle: LineDrawStyle,
    textStyle: TextDrawStyle,
    pointSpacing: Int = 1,
    dataXRange: ClosedFloatingPointRange<Float>,
    dataYRange: ClosedFloatingPointRange<Float>,
    function: (x: Float) -> Float
) {
    Canvas(
        modifier
            .padding(16.dp)
            .padding(top = if (chartName.isNotEmpty()) 16.dp else 0.dp)
            .fillMaxSize()
    ) {
        val points = mutableListOf<Point>()
        fun addPoint(x: Float, y: Float) = points.add(x by y)
        (0..size.width.toInt() step pointSpacing.coerceAtLeast(1)).forEach {
            addPoint(it.toFloat(), function(convertCanvasXToDataX(it, dataXRange)))
        }
        addPoint(size.width, function(dataXRange.endInclusive))

        points.createOffsets(this, dataYRange)

        drawChartTitle(chartName, textStyle)
        drawFrame()
        drawZeroLines(dataXRange, dataYRange)

        clipRect(
            -1.dp.toPx(),
            -1.dp.toPx(),
            size.width + 1.dp.toPx(),
            size.height + 1.dp.toPx()
        ) {
            drawLines(points, lineStyle)
        }

        drawMinMaxAxisValues(
            dataXRange.start,
            dataXRange.endInclusive,
            dataYRange.start,
            dataYRange.endInclusive,
            textStyle
        )
    }
}
