package com.gabrieldrn.konstellation.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import com.gabrieldrn.konstellation.plotting.Point

/**
 * A scope providing placement and contents data for a highlighted composed content.
 * @property point The [Point] to highlight.
 * @property contentPosition Where the content will be gravitating around the point, in front of the
 * chart.
 * @property chartPaddings The chart's paddings.
 */
class HighlightScope(
    val point: Point,
    val contentPosition: HighlightContentPosition,
    private val chartPaddings: PaddingValues
) {
    internal var paddingTop = 0
    internal var paddingStart = 0

    /**
     * Returns an offset helping to place the highlight content near the highlighted point
     * depending on the [contentPosition].
     */
    fun getContentOffset(): IntOffset = when (contentPosition) {
        HighlightContentPosition.Top,
        HighlightContentPosition.Point ->
            IntOffset(point.xPos.toInt() + paddingStart, point.yPos.toInt() + paddingTop)
        HighlightContentPosition.Bottom ->
            IntOffset(point.xPos.toInt() + paddingStart, 0)
        HighlightContentPosition.Start ->
            IntOffset(0, point.yPos.toInt())
        HighlightContentPosition.End ->
            IntOffset(0, point.yPos.toInt())
    }

    /**
     * Computes the paddings to be used when placing the highlight content.
     */
    @Composable
    fun ComputePaddings() {
        LocalDensity.current.run {
            paddingTop = chartPaddings.calculateTopPadding().toPx().toInt()
            paddingStart = chartPaddings.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
        }
    }
}
