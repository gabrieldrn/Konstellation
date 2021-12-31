package com.gabrieldrn.konstellation.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.plotting.Point

/**
 * A scope providing placement and contents data for a highlighting composed content.
 * @param point The [Point] to highlight.
 * @param position Where the content will be gravitating in front of the chart.
 */
class HighlightScope(
    val point: Point,
    val position: HighlightPosition,
    private val chartPaddings: PaddingValues
) {
    internal var paddingTop = 0
    internal var paddingStart = 0

    val popupPositioner: Density.() -> IntOffset = {
        when (position) {
            HighlightPosition.TOP, HighlightPosition.POINT -> {
                IntOffset(
                    point.xPos.toInt() + paddingStart,
                    point.yPos.toInt() + paddingTop
                )
            }
            HighlightPosition.BOTTOM -> IntOffset(point.xPos.toInt() + paddingStart, 0)
            HighlightPosition.START -> IntOffset(0, point.yPos.toInt())
            HighlightPosition.END -> IntOffset(0, point.yPos.toInt())
        }
    }

    @Composable
    fun ComputePaddings() {
        LocalDensity.current.run {
            paddingTop = chartPaddings.calculateTopPadding().toPx().toInt()
            paddingStart = chartPaddings.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
        }
    }
}
