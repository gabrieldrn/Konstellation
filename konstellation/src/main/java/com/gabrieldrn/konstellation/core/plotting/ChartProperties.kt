package com.gabrieldrn.konstellation.core.plotting

import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

internal interface ChartProperties {
    val lineStyle: LineDrawStyle
    val pointStyle: PointDrawStyle
    val dataXRange: ClosedFloatingPointRange<Float>?
    val dataYRange: ClosedFloatingPointRange<Float>?
}

data class LineChartProperties(
    override var lineStyle: LineDrawStyle = LineDrawStyle(),
    override var pointStyle: PointDrawStyle = PointDrawStyle(),
    var textStyle: TextDrawStyle = TextDrawStyle(),
    var highlightPointStyle: PointDrawStyle = PointDrawStyle(),
    var highlightTextStyle: TextDrawStyle = TextDrawStyle(),
    override var dataXRange: ClosedFloatingPointRange<Float>? = null,
    override var dataYRange: ClosedFloatingPointRange<Float>? = null,
) : ChartProperties
