package com.gabrieldrn.konstellation.charts.line.configuration

import android.graphics.Typeface
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.ChartStyles
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.setColor

/**
 * TODO DOC
 * @property lineStyle Appearance of the lines connecting points.
 * @property pointStyle Appearance of data points.
 * @property textStyle Text appearance of all texts.
 * @property highlightPointStyle Appearance of the highlighted point.
 * @property highlightLineStyle Appearance of the lines drawn on the highlighted point. Their
 * orientation depends on the provided highlighting positions in the LineChart composable.
 * @property highlightTextStyle Text appearance of the text shown in the highlight popup.
 */
data class LineChartStyles(
    override val lineStyle: LineDrawStyle = LineDrawStyle(),
    override val pointStyle: PointDrawStyle = PointDrawStyle(),
    override val xAxisBottomStyle: AxisDrawStyle = Axes.xBottomStyle,
    override val xAxisTopStyle: AxisDrawStyle = Axes.xTopStyle,
    override val yAxisLeftStyle: AxisDrawStyle = Axes.yLeftStyle,
    override val yAxisRightStyle: AxisDrawStyle = Axes.yRightStyle,
    val textStyle: TextDrawStyle = TextDrawStyle(),
    val highlightPointStyle: PointDrawStyle = PointDrawStyle(),
    val highlightLineStyle: LineDrawStyle = LineDrawStyle(
        strokeWidth = 1.dp, dashed = true
    ),
    val highlightTextStyle: TextDrawStyle = TextDrawStyle(),
) : ChartStyles {

    /**
     * Set of all axes styles.
     */
    val axes get() = setOf(xAxisBottomStyle, xAxisTopStyle, yAxisLeftStyle, yAxisRightStyle)
}

/**
 * Convenience function to change the typeface of texts drawn on axes.
 * @param typeface The new typeface to apply.
 */
fun LineChartStyles.setAxesTypeface(typeface: Typeface) {
    axes.forEach { it.tickTextStyle.typeface = typeface }
}

fun LineChartStyles.setAxesColor(color: Color) {
    axes.forEach { it.setColor(color) }
}
