package com.gabrieldrn.konstellation.plotting

import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle

interface ChartProperties {
    val lineStyle: LineDrawStyle
    val pointStyle: PointDrawStyle
    val dataXRange: ClosedFloatingPointRange<Float>?
    val dataYRange: ClosedFloatingPointRange<Float>?
    val axes: Set<ChartAxis>
}
