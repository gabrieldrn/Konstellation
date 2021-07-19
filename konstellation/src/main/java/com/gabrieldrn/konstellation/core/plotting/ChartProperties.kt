package com.gabrieldrn.konstellation.core.plotting

import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle

internal interface ChartProperties {
    val lineStyle: LineDrawStyle
    val pointStyle: PointDrawStyle
    val dataXRange: ClosedFloatingPointRange<Float>?
    val dataYRange: ClosedFloatingPointRange<Float>?
    val axes: Set<ChartAxis>
}
