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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.offsetsFromPoints
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.drawFrame
import com.gabrieldrn.konstellation.util.drawMiddleHorizontalLine
import com.gabrieldrn.konstellation.util.drawMiddleVerticalLine
import com.gabrieldrn.konstellation.util.drawText

/**
 * Composable responsible of plotting points and draw axis.
 */
@Composable
internal fun DataSetPlotter(
    dataSet: Array<Vertex>,
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
    drawStyle: LineDrawStyle,
    textStyle: TextDrawStyle,
    precision: Int = 10,
    function: (size: Size, x: Int) -> Float
) {
    val typo = MaterialTheme.typography
    val points = mutableListOf<Offset>()
    fun addPoint(x: Float, y: Float) = points.add(Offset(x, y))
    Canvas(modifier.fillMaxSize()) {
        drawFrame()
        drawMiddleHorizontalLine()
        drawMiddleVerticalLine()
        val width = size.width.toInt()
        (0..width step precision).forEach {
            addPoint(it.toFloat(), function(size, it))
        }
        addPoint(width.toFloat(), function(size, width))
        drawPoints(
            points = points,
            strokeWidth = 4f,
            pointMode = PointMode.Polygon,
            color = Color.Blue
        )
        points.forEach {
            drawText(it,"${it.x}, ${it.y}", textAlign = Paint.Align.LEFT,  typeface = textStyle.typeface)
        }

    }
}
