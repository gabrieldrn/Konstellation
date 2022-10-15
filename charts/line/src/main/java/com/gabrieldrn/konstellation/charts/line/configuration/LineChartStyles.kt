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
 * Class defining visual appearances of components in a Konstellation line chart.
 * @property lineStyle Appearance of the lines connecting data points.
 * @property pointStyle Appearance of data points.
 * @property xAxisBottomStyle Appearance of the bottom axis.
 * @property xAxisTopStyle Appearance of the top axis.
 * @property yAxisLeftStyle Appearance of the left axis.
 * @property yAxisRightStyle Appearance of the right axis.
 * @property textStyle Text appearance of all texts.
 * @property highlightPointStyle Appearance of the highlighted data point.
 * @property highlightLineStyle Appearance of the lines drawn on the highlighted point. Their
 * orientation depends on the provided highlighting positions in the LineChart composable.
 * @property highlightTextStyle Text appearance of the text shown in the highlight popup.
 */
data class LineChartStyles(
    override val lineStyle: LineDrawStyle = defaultLineStyle,
    override val pointStyle: PointDrawStyle = defaultPointStyle,
    override val xAxisBottomStyle: AxisDrawStyle = defaultXAxisBottomStyle,
    override val xAxisTopStyle: AxisDrawStyle = defaultXAxisTopStyle,
    override val yAxisLeftStyle: AxisDrawStyle = defaultYAxisLeftStyle,
    override val yAxisRightStyle: AxisDrawStyle = defaultYAxisRightStyle,
    val textStyle: TextDrawStyle = defaultTextStyle,
    val highlightPointStyle: PointDrawStyle = defaultHighlightPointStyle,
    val highlightLineStyle: LineDrawStyle? = defaultHighlightLineStyle,
    val highlightTextStyle: TextDrawStyle = defaultHighlightTextStyle,
) : ChartStyles {

    /**
     * Set of all axes styles.
     */
    val axesStyles get() = setOf(xAxisBottomStyle, xAxisTopStyle, yAxisLeftStyle, yAxisRightStyle)

    companion object {
        val defaultLineStyle = LineDrawStyle()
        val defaultPointStyle = PointDrawStyle()
        val defaultXAxisBottomStyle = Axes.xBottomStyle
        val defaultXAxisTopStyle = Axes.xTopStyle
        val defaultYAxisLeftStyle = Axes.yLeftStyle
        val defaultYAxisRightStyle = Axes.yRightStyle
        val defaultTextStyle = TextDrawStyle()
        val defaultHighlightPointStyle = PointDrawStyle()
        val defaultHighlightLineStyle = LineDrawStyle(strokeWidth = 1.dp, dashed = true)
        val defaultHighlightTextStyle = TextDrawStyle()
    }
}

/**
 * Convenience function to change the typeface of texts drawn on axes.
 * @param typeface The new typeface to apply.
 */
fun LineChartStyles.setAxesTypeface(typeface: Typeface) {
    axesStyles.forEach { it.tickTextStyle.typeface = typeface }
}

/**
 * Convenience function to change the color of all drawn axes.
 * @throws color The new color to apply.
 */
fun LineChartStyles.setAxesColor(color: Color) {
    axesStyles.forEach { it.setColor(color) }
}
