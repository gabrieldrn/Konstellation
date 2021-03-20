package com.gabrieldrn.konstellation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private typealias PlotPoint = Pair<Float, Float>

private val PlotPoint.x get() = first
private val PlotPoint.y get() = second

private fun DrawScope.convertPointsToOffsets(
    points: Array<PlotPoint>
): List<Offset> {
    val xMax = points.map { it.x }.maxOrNull() ?: 0f
    val yMax = points.map { it.y }.maxOrNull() ?: 0f
    return points.map {
        Offset(
            x = (it.x / xMax) * size.width,
            y = ((yMax - it.y) * size.height) / yMax
        )
    }
}

private fun createDataSet() = mutableListOf<PlotPoint>().apply {
    repeat(25) {
        add(it.toFloat() to Random.nextFloat())
    }
}.toTypedArray()

@Composable
fun KonstellationCanvas(modifier: Modifier = Modifier) {
//    val points = arrayOf(
//        0f to 0f,
//        25f to 50f,
//        50f to 200f,
//        75f to 150f,
//        100f to 175f
//    )
    var points = createDataSet()
    Surface {
        Column(modifier = modifier
            .padding(12.dp)
            .fillMaxSize()) {
            Row(modifier = Modifier.weight(1f)) {
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    drawPoints(
                        points = convertPointsToOffsets(points),
                        pointMode = PointMode.Polygon,
                        color = Color.Black,
                        strokeWidth = 5f
                    )
                }
                Column(
                    Modifier
                        .width(100.dp)
                        .background(Color.Gray)
                ) {}
            }
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()) {}
            Row {
                Button(onClick = {
                    points = createDataSet()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "New dataset")
                }
            }
        }
    }
}

@Preview
@Composable
fun CanvasPreview() {
    KonstellationCanvas()
}