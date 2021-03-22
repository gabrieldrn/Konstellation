package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.data.offsetsFromPoints
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.drawFrame
import com.gabrieldrn.konstellation.util.drawMiddleHorizontalLine
import com.gabrieldrn.konstellation.util.drawMiddleVerticalLine
import com.gabrieldrn.konstellation.util.drawText
import com.gabrieldrn.konstellation.util.toInt

/**
 * Composable responsible of plotting points and draw axis.
 */
@Composable
internal fun DataSetPlotter(
    dataSet: Collection<Vertex>,
    drawStyle: LineDrawStyle,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.weight(1f)) {
            Column(
                Modifier
                    .width(100.dp)
                    .background(Color.Gray)
            ) {}
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                drawPoints(
                    points = offsetsFromPoints(dataSet),
                    pointMode = PointMode.Polygon,
                    color = drawStyle.color,
                    strokeWidth = drawStyle.strokeWidth
                )
            }
        }
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {}
    }
}

/**
 * Composable plotting a function.
 */
@Composable
fun FunctionPlotter(
    modifier: Modifier = Modifier,
    lineStyle: LineDrawStyle,
    textStyle: TextDrawStyle,
    precision: Int = 1,
    dataXRange: ClosedFloatingPointRange<Float>,
    function: (x: Float) -> Float
) {
    val vertex = mutableListOf<Vertex>()
    fun addPoint(x: Float, y: Float) = vertex.add(x by y)
    Canvas(modifier.fillMaxSize()) {
        drawFrame()
        drawMiddleHorizontalLine()
        drawMiddleVerticalLine()

        (0..size.width.toInt() step precision).forEach {
            addPoint(it.toFloat(), function(convertCanvasXToDataX(it, dataXRange)))
        }
        //addPoint(size.width, function(dataXRange.endInclusive))

        val points = offsetsFromPoints(vertex)

        drawPoints(
            points = points,
            strokeWidth = lineStyle.strokeWidth,
            pointMode = PointMode.Polygon,
            color = lineStyle.color
        )
//        points.forEach {
//            drawText(it, 10f, -10f, text = "${it.y}", textAlign = Paint.Align.LEFT, typeface = textStyle.typeface)
//        }
        val yMax = vertex.map { it.y }.maxOrNull() ?: 0f
        val yMin = vertex.map { it.y }.minOrNull() ?: 0f
        drawText(
            Offset(0f, 0f),
            offsetY = 25f,
            text = yMin.toString(),
            textAlign = Paint.Align.LEFT,
            typeface = textStyle.typeface,
            color = textStyle.color.toInt()
        )
        drawText(
            Offset(0f, size.height),
            text = yMax.toString(),
            textAlign = Paint.Align.LEFT,
            typeface = textStyle.typeface,
            color = textStyle.color.toInt()
        )
    }
}
