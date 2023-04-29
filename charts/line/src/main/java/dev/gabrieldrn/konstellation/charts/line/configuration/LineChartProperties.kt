package dev.gabrieldrn.konstellation.charts.line.configuration

import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.math.MonotoneXPathInterpolator
import dev.gabrieldrn.konstellation.charts.line.math.PathInterpolator
import dev.gabrieldrn.konstellation.configuration.properties.ChartProperties
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.highlighting.HighlightLinePosition
import dev.gabrieldrn.konstellation.plotting.Axes
import dev.gabrieldrn.konstellation.plotting.ChartAxis
import java.io.Serializable

/**
 * Class defining features of a Konstellation line chart.
 * @property axes Axes to be drawn on the chart.
 * @property chartPaddingValues Paddings applied to the bounds of the chart (from "view" bounds to
 * axes).
 * @property chartWindow The visualization window of the chart. If null, the chart will be drawn
 * with a window that fits all the data.
 * @property highlightContentPositions Where to place highlighting popups. There will be as many
 * popups as there are positions.
 * @property highlightLinePosition Where highlight lines will be placed. See enum values for more
 * insight. A null value means not drawing any highlight line.
 * @property hapticHighlight Either to perform haptic feedbacks every time a new value is
 * highlighted.
 * @property drawFrame Either to draw the lines delimiting the chart or not.
 * @property drawZeroLines Either to draw the lines indicating the zero on X and Y axes or not.
 * @property drawLines Either to draw lines (as described by [LineChartStyles.lineStyle]) or not.
 * @property drawPoints Either to draw points (as described by [LineChartStyles.pointStyle]) or not.
 * @property pathInterpolator The interpolator to use when drawing the lines.
 * @property fillingBrush The brush to apply to the filling content from the bottom of the chart to
 * the data lines.
 * @property enablePanning Either to enable panning or not.
 */
public data class LineChartProperties(
    override val axes: Set<ChartAxis> = setOf(Axes.xBottom, Axes.yLeft),
    val chartPaddingValues: PaddingValues = PaddingValues(40.dp),
    val chartWindow: ChartWindow? = null,
    val highlightContentPositions: Set<HighlightContentPosition> = setOf(
        HighlightContentPosition.Point
    ),
    val highlightLinePosition: HighlightLinePosition? = HighlightLinePosition.Relative,
    override val hapticHighlight: Boolean = false,
    override val drawFrame: Boolean = true,
    override val drawZeroLines: Boolean = true,
    val drawLines: Boolean = true,
    val drawPoints: Boolean = true,
    val pathInterpolator: PathInterpolator = MonotoneXPathInterpolator(),
    val fillingBrush: Brush? = null,
    val enablePanning: Boolean = true,
) : ChartProperties, Serializable {
    private companion object {
        private const val serialVersionUID: Long = 1L
    }
}
