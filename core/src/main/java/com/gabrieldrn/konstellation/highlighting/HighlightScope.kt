package com.gabrieldrn.konstellation.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.plotting.Point

/**
 * A scope providing placement and contents data for a highlighted composed content.
 * @param point The [Point] to highlight.
 * @param contentPosition Where the content will be gravitating in front of the chart.
 */
class HighlightScope(
    val point: Point,
    val contentPosition: HighlightContentPosition,
    private val chartPaddings: PaddingValues
) {
    internal var paddingTop = 0
    internal var paddingStart = 0

    val popupPositioner: Density.() -> IntOffset = {
        when (contentPosition) {
            HighlightContentPosition.TOP, HighlightContentPosition.POINT -> {
                IntOffset(
                    point.xPos.toInt() + paddingStart,
                    point.yPos.toInt() + paddingTop
                )
            }
            HighlightContentPosition.BOTTOM -> IntOffset(point.xPos.toInt() + paddingStart, 0)
            HighlightContentPosition.START -> IntOffset(0, point.yPos.toInt())
            HighlightContentPosition.END -> IntOffset(0, point.yPos.toInt())
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
