package dev.gabrieldrn.konstellation.charts.line.style

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import dev.gabrieldrn.konstellation.charts.line.math.MonotoneXPathInterpolator
import dev.gabrieldrn.konstellation.charts.line.math.PathInterpolator
import dev.gabrieldrn.konstellation.configuration.styles.AxisDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.ChartStyles
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import dev.gabrieldrn.konstellation.plotting.Axes
import dev.gabrieldrn.konstellation.plotting.ChartAxis

/**
 * 🎨 Class defining the visual styles of a Konstellation line chart. They only affect the
 * appearance of the chart.
 *
 * @property axes Axes to be drawn on the chart.
 * @property drawLines Either to draw lines (as described by [LineChartStyles.lineStyle]) or not.
 * @property drawPoints Either to draw points (as described by [LineChartStyles.pointStyle]) or not.
 * @property drawFrame Either to draw the lines delimiting the chart or not.
 * @property drawZeroLines Either to draw the lines indicating the zero on X and Y axes or not.
 * @property lineStyle Appearance of the lines connecting data points.
 * @property pointStyle Appearance of data points.
 * @property xAxisBottomStyle Appearance of the bottom axis.
 * @property xAxisTopStyle Appearance of the top axis.
 * @property yAxisLeftStyle Appearance of the left axis.
 * @property yAxisRightStyle Appearance of the right axis.
 * @property textStyle Text appearance of all texts.
 * @property fillingBrush The brush to apply to the filling content from the bottom of the chart to
 * the data lines. If null, no filling is applied.
 * @property pathInterpolator The interpolator to use when drawing the lines.
 */
@Immutable
public data class LineChartStyles(
    override val axes: Set<ChartAxis> = setOf(Axes.xBottom, Axes.yLeft),
    val drawLines: Boolean = true,
    val drawPoints: Boolean = true,
    override val drawFrame: Boolean = true,
    override val drawZeroLines: Boolean = true,
    override val lineStyle: LineDrawStyle = LineDrawStyle(),
    override val pointStyle: PointDrawStyle = PointDrawStyle(),
    override val xAxisBottomStyle: AxisDrawStyle = Axes.xBottomStyle,
    override val xAxisTopStyle: AxisDrawStyle = Axes.xTopStyle,
    override val yAxisLeftStyle: AxisDrawStyle = Axes.yLeftStyle,
    override val yAxisRightStyle: AxisDrawStyle = Axes.yRightStyle,
    val textStyle: TextDrawStyle = TextDrawStyle(),
    val fillingBrush: Brush? = null,
    val pathInterpolator: PathInterpolator = MonotoneXPathInterpolator(),
) : ChartStyles
