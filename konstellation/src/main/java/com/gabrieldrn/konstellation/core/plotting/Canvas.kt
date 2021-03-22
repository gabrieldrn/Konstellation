package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.randomDataSet
import kotlin.math.PI
import kotlin.math.sin

/**
 * Composable responsible of plotting points [points].
 *
 * @param points Points to draw.
 * @param padding Spacing around the canvas.
 * @param modifier
 */
@Composable
fun KonstellationCanvas(
    points: Array<Vertex>,
    modifier: Modifier = Modifier,
    drawStyle: LineDrawStyle = LineDrawStyle(),
    textStyle: TextDrawStyle = TextDrawStyle(Typeface.DEFAULT),
    padding: Dp = 12.dp,
    doFillMaxSize: Boolean = true,
) {
    val m = modifier.padding(padding).apply { if (doFillMaxSize) fillMaxSize() }
    //DataSetPlotter(points = points, drawStyle = drawStyle, modifier = m)
//    FunctionPlotter(m, drawStyle, textStyle) { size, x ->
//        (sin(x * (2f * PI / size.width)) * (size.height / 2)  + (size.height / 2)).toFloat()
//    }
}

@Composable
fun KonstellationCanvas(
    modifier: Modifier = Modifier,
    drawStyle: LineDrawStyle = LineDrawStyle(),
    textStyle: TextDrawStyle = TextDrawStyle(Typeface.DEFAULT),
    precision: Int = 10,
    padding: Dp = 12.dp,
    doFillMaxSize: Boolean = true,
    xRange: ClosedFloatingPointRange<Float> = 0f..1f,
    function: (x: Float) -> Float
) {
    val m = modifier.padding(padding).apply { if (doFillMaxSize) fillMaxSize() }
    FunctionPlotter(m, drawStyle, textStyle, precision = precision, dataXRange = xRange, function = function)
}

@Preview
@Composable
fun CanvasPreview() {
    Surface {
        KonstellationCanvas(xRange = -PI.toFloat()..PI.toFloat()) {
            sin(2 * it)
        }
    }
}
