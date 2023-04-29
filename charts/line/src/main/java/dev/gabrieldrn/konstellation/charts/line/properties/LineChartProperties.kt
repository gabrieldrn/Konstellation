package dev.gabrieldrn.konstellation.charts.line.properties

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.properties.ChartProperties
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.highlighting.HighlightLinePosition

/**
 * ⚙️ Class defining the properties of a Konstellation line chart, affecting how the chart is
 * calculated. They do not affect the data itself.
 *
 * ⚠️ **Calculating the chart is an expensive process. Changing a property implies to recalculate
 * the entire chart.**
 *
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
 * @property enablePanning Either to enable panning or not.
 */
@Stable
public data class LineChartProperties(
    val chartPaddingValues: PaddingValues = PaddingValues(40.dp),
    val chartWindow: ChartWindow? = null,
    val highlightContentPositions: Set<HighlightContentPosition> = setOf(
        HighlightContentPosition.Point
    ),
    val highlightLinePosition: HighlightLinePosition? = HighlightLinePosition.Relative,
    override val hapticHighlight: Boolean = false,
    val enablePanning: Boolean = true,
) : ChartProperties
