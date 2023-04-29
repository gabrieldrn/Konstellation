package dev.gabrieldrn.konstellation.charts.line.configuration

import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.ChartStyles
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import dev.gabrieldrn.konstellation.plotting.Axes

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
 */
public data class LineChartStyles(
    override val lineStyle: LineDrawStyle = LineDrawStyle(),
    override val pointStyle: PointDrawStyle = PointDrawStyle(),
    override val xAxisBottomStyle: AxisDrawStyle = Axes.xBottomStyle,
    override val xAxisTopStyle: AxisDrawStyle = Axes.xTopStyle,
    override val yAxisLeftStyle: AxisDrawStyle = Axes.yLeftStyle,
    override val yAxisRightStyle: AxisDrawStyle = Axes.yRightStyle,
    val textStyle: TextDrawStyle = TextDrawStyle(),
    val highlightPointStyle: PointDrawStyle = PointDrawStyle(),
    val highlightLineStyle: LineDrawStyle? = LineDrawStyle(strokeWidth = 1.dp, dashed = true),
) : ChartStyles
