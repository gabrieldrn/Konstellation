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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.convertCanvasXToDataX
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

/**
 * Composable responsible of plotting lines from a dataset and draw axis.
 */
@Composable
fun LinePlotter(
    dataSet: Dataset,
    modifier: Modifier = Modifier,
    lineStyle: LineDrawStyle = LineDrawStyle(),
    pointStyle: PointDrawStyle = PointDrawStyle(),
    textStyle: TextDrawStyle = TextDrawStyle(),
    dataYRange: ClosedFloatingPointRange<Float>,
) {
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }
    val pointerListener: (MotionEvent) -> Boolean = {
        when (it.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                highlightedValue = dataSet.nearestPointByX(it.x)
                true
            }
            else -> false
        }
    }
    val highlightPointStyle = pointStyle.copy(radius = 7.dp)

    Canvas(
        modifier
            .padding(16.dp)
            .fillMaxSize()
            .pointerInteropFilter(null, pointerListener)
    ) {
        dataSet.createOffsets(this, dataYRange)
        drawFrame()
        drawZeroLines(dataSet.xRange, dataYRange)
        drawLines(dataSet, lineStyle, pointStyle, drawPoints = true)
        highlightedValue?.let {
            highlightPoint(it, highlightPointStyle, textStyle)
        }
        drawMinMaxAxisValues(
            dataSet.xMin,
            dataSet.xMax,
            dataYRange.start,
            dataYRange.endInclusive,
            textStyle
        )
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

        points.createOffsets(this, dataYRange)

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

internal class OffsetComparable : Comparator<Offset> {
    override fun compare(o1: Offset?, o2: Offset?): Int {
        requireNotNull(o1)
        requireNotNull(o2)
        return o1.x.compareTo(o2.x)
    }
}
