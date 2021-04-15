package com.gabrieldrn.konstellation.core.plotting

import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.style.AxisDrawStyle

internal interface ChartProperties {
    val lineStyle: LineDrawStyle
    val pointStyle: PointDrawStyle
    val dataXRange: ClosedFloatingPointRange<Float>?
    val dataYRange: ClosedFloatingPointRange<Float>?
    val xAxisDrawStyle: AxisDrawStyle
    val yAxisDrawStyle: AxisDrawStyle
    val xAxisTickCount: Int
    val yAxisTickCount: Int
}

data class LineChartProperties(
    override var lineStyle: LineDrawStyle = LineDrawStyle(),
    override var pointStyle: PointDrawStyle = PointDrawStyle(),
    var textStyle: TextDrawStyle = TextDrawStyle(),
    var highlightPointStyle: PointDrawStyle = PointDrawStyle(),
    var highlightTextStyle: TextDrawStyle = TextDrawStyle(),
    override var dataXRange: ClosedFloatingPointRange<Float>? = null,
    override var dataYRange: ClosedFloatingPointRange<Float>? = null,
    override val xAxisDrawStyle: AxisDrawStyle = AxisDrawStyle(),
    override val yAxisDrawStyle: AxisDrawStyle = AxisDrawStyle(),
    override val xAxisTickCount: Int = 5,
    override val yAxisTickCount: Int = 5,
) : ChartProperties
