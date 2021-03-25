package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.drawFrame
import com.gabrieldrn.konstellation.util.drawLines
import com.gabrieldrn.konstellation.util.drawMiddleHorizontalLine
import com.gabrieldrn.konstellation.util.drawMiddleVerticalLine
import com.gabrieldrn.konstellation.util.drawMinMaxAxisValues
import com.gabrieldrn.konstellation.util.drawText
import com.gabrieldrn.konstellation.util.toInt

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
            .fillMaxSize()
    ) {
        drawFrame()
        drawMiddleHorizontalLine()
        drawMiddleVerticalLine()
        drawLines(dataSet.createOffsets(this), lineStyle)
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
        val rawPoints = mutableListOf<Point>()
        fun addPoint(x: Float, y: Float) = rawPoints.add(x by y)
        (0..size.width.toInt() step pointSpacing.coerceAtLeast(1)).forEach {
            addPoint(it.toFloat(), function(convertCanvasXToDataX(it, dataXRange)))
        }
        addPoint(size.width, function(dataXRange.endInclusive))

        val points = rawPoints.createOffsets(this, dataYRange).toList()

        drawText(
            Offset(size.width / 2, 0f),
            text = chartName,
            offsetY = -25f,
            textAlign = Paint.Align.CENTER,
            textSize = 50f,
            typeface = textStyle.typeface,
            color = textStyle.color.toInt()
        )
        drawFrame()
        drawMiddleHorizontalLine()
        drawMiddleVerticalLine()
        drawLines(points, lineStyle)
        //drawLabelPoints(points, textStyle)

        drawMinMaxAxisValues(
            dataXRange.start,
            dataXRange.endInclusive,
            dataYRange.start,
            dataYRange.endInclusive,
            textStyle
        )
    }
}
