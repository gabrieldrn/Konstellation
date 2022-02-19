package com.gabrieldrn.konstellation.charts.function

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.drawing.*
import com.gabrieldrn.konstellation.geometry.convertCanvasXToDataX
import com.gabrieldrn.konstellation.geometry.createOffsets
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle

/**
 * Composable responsible for plotting points from a function and drawing axis.
 */
@Composable
@Suppress("LongParameterList") //TODO Remove Suppress
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
            addPoint(it.toFloat(), function(convertCanvasXToDataX(it.toFloat(), dataXRange)))
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

fun DrawScope.drawMinMaxAxisValues(
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
