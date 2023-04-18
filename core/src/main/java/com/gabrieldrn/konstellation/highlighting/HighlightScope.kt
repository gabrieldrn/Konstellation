package com.gabrieldrn.konstellation.highlighting

import com.gabrieldrn.konstellation.plotting.Point

/**
 * A scope providing placement and contents data for a highlighted composed content.
 * @property point The [Point] to highlight.
 * @property contentPosition Where the content will be gravitating around the point, in front of the
 * chart.
 */
data class HighlightScope(
    val point: Point,
    val contentPosition: HighlightContentPosition
)
