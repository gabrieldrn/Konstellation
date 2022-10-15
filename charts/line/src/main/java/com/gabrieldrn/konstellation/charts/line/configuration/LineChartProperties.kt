package com.gabrieldrn.konstellation.charts.line.configuration

import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.configuration.properties.ChartProperties
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightLinePosition

/**
 * Class defining features of a Konstellation line chart.
 * @property axes Axes to be drawn on the chart.
 * @property chartPaddingValues Paddings applied to the bounds of the chart (from "view" bounds to
 * axes).
 * @property datasetOffsets Offsets to be applied around the dataset, see [DatasetOffsets]
 * for more details. A null value means no offsets.
 * @property highlightContentPositions Where to place highlighting popups. There will be as many
 * popups as there are positions.
 * @property highlightLinePosition Where highlight lines will be placed. See enum values for more
 * insight. A null value means not drawing any highlight line.
 * @property drawLines Either to draw lines (as described by [LineChartStyles.lineStyle]) or not.
 * @property drawPoints Either to draw points (as described by [LineChartStyles.pointStyle]) or not.
 * @property smoothing The style of the lines drawing between the data points.
 * @property fillingBrush The brush to apply to the filling content from the bottom of the chart to
 * the data lines.
 */
data class LineChartProperties(
    override val axes: Set<ChartAxis> = setOf(Axes.xBottom, Axes.yLeft),
    val chartPaddingValues: PaddingValues = PaddingValues(44.dp),
    val datasetOffsets: DatasetOffsets? = null,
    val highlightContentPositions: Set<HighlightContentPosition> = setOf(
        HighlightContentPosition.Point
    ),
    val highlightLinePosition: HighlightLinePosition? = HighlightLinePosition.Relative,
    val drawLines: Boolean = true,
    val drawPoints: Boolean = false,
    override val smoothing: Smoothing = Smoothing.values().first(),
    val fillingBrush: Brush? = null
) : ChartProperties
