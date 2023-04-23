package com.gabrieldrn.konstellation.charts.function

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellation.drawing.drawFrame
import com.gabrieldrn.konstellation.drawing.drawLines
import com.gabrieldrn.konstellation.drawing.drawText
import com.gabrieldrn.konstellation.drawing.drawZeroLines
import com.gabrieldrn.konstellation.math.createOffsets
import com.gabrieldrn.konstellation.math.mapCanvasXToDataX
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.by

/**
 * Composable responsible for plotting points from a function and drawing axis.
 */
@WipChartApi
@Composable
@Suppress("LongParameterList") //TODO Remove Suppress
public fun FunctionPlotter(
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
            addPoint(it.toFloat(), function(mapCanvasXToDataX(it.toFloat(), dataXRange)))
        }
        addPoint(size.width, function(dataXRange.endInclusive))

        val computedDataSet = points.createOffsets(size, xWindowRange = dataYRange)

        drawFrame()
        drawZeroLines(dataXRange, dataYRange)
        drawLines(computedDataSet, lineStyle, pointStyle, false)
        drawMinMaxAxisValues(
            dataXRange.start,
            dataXRange.endInclusive,
            dataYRange.start,
            dataYRange.endInclusive,
            textStyle
        )
    }
}

private fun DrawScope.drawMinMaxAxisValues(
    xMin: Number, xMax: Number,
    yMin: Number, yMax: Number,
    textStyle: TextDrawStyle
) {
    drawText(
        Offset(0f, size.height),
        text = xMin.toString(),
        style = textStyle.copy(offsetY = 25f)
    )
    drawText(
        Offset(size.width, size.height),
        text = xMax.toString(),
        style = textStyle.copy(textAlign = Paint.Align.RIGHT, offsetY = 25f)
    )
    drawText(
        Offset(0f, size.height),
        text = yMin.toString(),
        style = textStyle
    )
    drawText(
        Offset(0f, 0f),
        text = yMax.toString(),
        style = textStyle.copy(offsetY = 25f)
    )
}
