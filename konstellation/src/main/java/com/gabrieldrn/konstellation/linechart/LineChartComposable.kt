package com.gabrieldrn.konstellation.linechart

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.core.drawing.*
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

/**
 * Lambda invoked when a point needs to be highlighted.
 */
val highlight: DrawScope.(Point, PointDrawStyle, TextDrawStyle) -> Unit = DrawScope::highlightPoint

/**
 * Composable responsible of plotting lines from a dataset and draw axis.
 */
@ExperimentalComposeUiApi
@Composable
fun LineChart(
    dataSet: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties()
) {
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }
    //Mandatory: Used to make the chart the only consumer of touch event within it.
    val disallowInterceptTouchEvent = RequestDisallowInterceptTouchEvent()
    val pointerListener: (MotionEvent) -> Boolean = {
        when (it.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                disallowInterceptTouchEvent(true)
                highlightedValue = dataSet.nearestPointByX(it.x)
                true
            }
            MotionEvent.ACTION_UP -> {
                disallowInterceptTouchEvent(false)
                true
            }
            else -> false
        }
    }

    val xRange = properties.dataXRange ?: dataSet.xRange
    val yRange = properties.dataYRange ?: dataSet.yRange

    Canvas(
        modifier
            .padding(44.dp)
            .pointerInteropFilter(disallowInterceptTouchEvent, pointerListener)
    ) {
        drawFrame()

        dataSet.createOffsets(
            drawScope = this,
            dataSetXRange = xRange,
            dataSetYRange = yRange
        )

        with(properties) {
            drawZeroLines(xRange, yRange)
            clipRect(0f, 0f, size.width, size.height) {
                drawLines(dataSet, lineStyle, pointStyle, drawPoints = true)
                highlightedValue?.let {
                    highlight(this@Canvas, it, highlightPointStyle, highlightTextStyle)
                }
            }
            drawScaledAxis(this, dataSet)
        }
    }
}