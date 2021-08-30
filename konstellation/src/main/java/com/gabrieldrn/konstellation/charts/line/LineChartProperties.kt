package com.gabrieldrn.konstellation.charts.line

import android.graphics.Typeface
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.core.plotting.ChartProperties
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

/**
 * Class defining visuals and features of a Konstellation line chart.
 */
data class LineChartProperties(
    override var lineStyle: LineDrawStyle = LineDrawStyle(),
    override var pointStyle: PointDrawStyle = PointDrawStyle(),
    var textStyle: TextDrawStyle = TextDrawStyle(),
    var highlightPointStyle: PointDrawStyle = PointDrawStyle(),
    var highlightTextStyle: TextDrawStyle = TextDrawStyle(),
    var chartPaddingValues: PaddingValues = PaddingValues(0.dp),
    override var dataXRange: ClosedFloatingPointRange<Float>? = null,
    override var dataYRange: ClosedFloatingPointRange<Float>? = null,
    override var axes: Set<ChartAxis> = setOf(Axes.xBottom, Axes.yLeft)
) : ChartProperties

fun LineChartProperties.setAxisTypeface(typeface: Typeface) {
    axes.forEach { it.style.tickTextStyle.typeface = typeface }
}
