package com.gabrieldrn.konstellation.charts.line

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.core.drawing.*
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.core.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.core.highlighting.HighlightPopupScope
import com.gabrieldrn.konstellation.core.highlighting.HighlightPosition

/**
 * Composable responsible of plotting lines from a dataset and draw axis.
 */
@ExperimentalComposeUiApi
@Composable
fun LineChart(
    dataSet: Dataset,
    modifier: Modifier = Modifier,
    properties: LineChartProperties = LineChartProperties(),
    highlightPositions: Array<HighlightPosition> = arrayOf(HighlightPosition.POINT),
    highlightContent: (@Composable HighlightPopupScope.(Point) -> Unit)? = null
) {
    var highlightedValue by rememberSaveable { mutableStateOf<Point?>(null) }
    //Mandatory: Used to make the chart as the only consumer of touch events within the tree.
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
                highlightedValue = null
                true
            }
            else -> false
        }
    }

    val xRange = properties.dataXRange ?: dataSet.xRange
    val yRange = properties.dataYRange ?: dataSet.yRange

    Box {
        Canvas(
            modifier
                .padding(properties.chartPaddingValues)
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
                        highlightPoint(it, highlightPositions, highlightPointStyle)
                    }
                }
                drawScaledAxis(this, dataSet)
            }
        }

        highlightedValue?.let { point ->
            if (highlightContent != null) {
                highlightPositions.forEach { position ->
                    val scope = HighlightPopupScope(
                        point, position, properties.chartPaddingValues
                    ).apply { ComputePaddings() }
                    HighlightPopup(scope) { point ->
                        highlightContent(point)
                    }
                }
            }
        }
    }
}
