package com.gabrieldrn.konstellation.core.plotting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.linechart.drawFrame
import com.gabrieldrn.konstellation.linechart.drawLines
import com.gabrieldrn.konstellation.linechart.drawMinMaxAxisValues
import com.gabrieldrn.konstellation.linechart.drawZeroLines
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

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
