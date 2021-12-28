package com.gabrieldrn.konstellation.charts.line.configuration

import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.configuration.properties.ChartProperties
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.highlighting.HighlightPosition

/**
 * Class defining features of a Konstellation line chart.
 * @property axes Axes to be drawn on the chart.
 * @property chartPaddingValues Paddings applied to the bounds of the chart (from "view" bounds to
 * axes)
 * @property datasetOffsets Offsets to be applied around the dataset, see [DatasetOffsets]
 * for more details. A null value means no offsets.
 * @property highlightPositions Where to place highlighting popups. There will be as much popups as
 * there is positions.
 */
data class LineChartProperties(
    override val axes: Set<ChartAxis> = setOf(Axes.xBottom, Axes.yLeft),
    val chartPaddingValues: PaddingValues = PaddingValues(0.dp),
    val datasetOffsets: DatasetOffsets? = null,
    val highlightPositions: Set<HighlightPosition> = setOf(HighlightPosition.POINT)
) : ChartProperties
