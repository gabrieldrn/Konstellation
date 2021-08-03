package com.gabrieldrn.konstellation.linechart

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.core.data.createOffsets
import com.gabrieldrn.konstellation.core.drawing.*
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.style.highlight.HighlightPopupShape
import com.gabrieldrn.konstellation.style.highlight.HighlightPosition

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
    properties: LineChartProperties = LineChartProperties(),
    highlightPosition: HighlightPosition = HighlightPosition.CENTER,
    highlightContent : (@Composable BoxScope.(Point) -> Unit)? = null
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

    val chartPadding = 44.dp
    val chartPaddingPx = LocalDensity.current.run { chartPadding.toPx() }.toInt()

    val xRange = properties.dataXRange ?: dataSet.xRange
    val yRange = properties.dataYRange ?: dataSet.yRange

    Box(Modifier.clip(CutCornerShape(0.dp))) {
        Canvas(
            modifier
                    .padding(chartPadding)
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

        highlightedValue?.let {
            if (highlightContent != null) {
                HighlightPopup(it, highlightPosition, chartPaddingPx, highlightContent)
            }
        }
    }
}

@Composable
fun HighlightPopup(
    point: Point,
    position: HighlightPosition,
    chartPadding: Int,
    content: @Composable BoxScope.(Point) -> Unit
) {
    fun getPlacementOffset(p: Placeable) = when (position) {
        HighlightPosition.TOP -> -IntOffset(p.width / 2, point.offset.y.toInt() + chartPadding)
        HighlightPosition.CENTER -> -IntOffset(p.width / 2, p.height)
        else -> IntOffset(0, 0) //TODO Implement placement of other positions
    }

    val popupLayoutModifier: MeasureScope.(Measurable, Constraints) -> MeasureResult = { m, c ->
        val placeable = m.measure(c)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(getPlacementOffset(placeable))
        }
    }

    val popupPositioner: Density.() -> IntOffset = {
        IntOffset(
            point.offset.x.toInt() + chartPadding, point.offset.y.toInt() + chartPadding
        )
    }

    Box(Modifier.layout(popupLayoutModifier)) {
        //TODO Make this Card customizable by the user.
        Card(Modifier
                .offset(popupPositioner)
                .padding(4.dp),
            backgroundColor = Color.White,
            shape = HighlightPopupShape(),
            elevation = 4.dp,
            content = { content(point) }
        )
    }
}
