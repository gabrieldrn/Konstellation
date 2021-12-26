package com.gabrieldrn.konstellation.charts.line

import android.graphics.Typeface
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.plotting.ChartProperties
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.style.setColor

/**
 * Class defining appearance and features of a Konstellation line chart.
 * @property lineStyle Appearance of the lines connecting points.
 * @property pointStyle Appearance of data points.
 * @property textStyle Text appearance of all texts.
 * @property highlightPointStyle Appearance of the highlighted point.
 * @property highlightLineStyle Appearance of the lines drawn on the highlighted point. Their
 * orientation depends on the provided highlighting positions in the LineChart composable.
 * @property highlightTextStyle Text appearance of the text shown in the highlight popup.
 * @property chartPaddingValues Paddings applied to the bounds of the chart (from "view" bounds to
 * axes)
 * @property dataXRange The range that should be applied to x axes. If null, data set range will be
 * used.
 * @property dataYRange The range that should be applied to y axes. If null, data set range will be
 * used.
 * @property axes Axes to be drawn on the chart.
 */
data class LineChartProperties(
    override var lineStyle: LineDrawStyle = LineDrawStyle(),
    override var pointStyle: PointDrawStyle = PointDrawStyle(),
    var textStyle: TextDrawStyle = TextDrawStyle(),
    var highlightPointStyle: PointDrawStyle = PointDrawStyle(),
    var highlightLineStyle: LineDrawStyle = LineDrawStyle(
        strokeWidth = 1.dp, dashed = true
    ),
    var highlightTextStyle: TextDrawStyle = TextDrawStyle(),
    var chartPaddingValues: PaddingValues = PaddingValues(0.dp),
    override var dataXRange: ClosedFloatingPointRange<Float>? = null,
    override var dataYRange: ClosedFloatingPointRange<Float>? = null,
    override var axes: Set<ChartAxis> = setOf(Axes.xBottom, Axes.yLeft)
) : ChartProperties

/**
 * Convenience function to change the typeface of texts drawn on axes.
 * @param typeface The new typeface to apply.
 */
fun LineChartProperties.setAxisTypeface(typeface: Typeface) {
    axes.forEach { it.style.tickTextStyle.typeface = typeface }
}

fun LineChartProperties.setAxesColor(color: Color) {
    axes.forEach { it.style.setColor(color) }
}
