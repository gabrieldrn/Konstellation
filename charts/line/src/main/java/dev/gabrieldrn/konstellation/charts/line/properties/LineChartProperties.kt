package dev.gabrieldrn.konstellation.charts.line.properties

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.properties.ChartProperties

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
 * @property enablePanning Either to enable panning or not.
 */
@Immutable
public data class LineChartProperties(
    val chartPaddingValues: PaddingValues = PaddingValues(40.dp),
    val chartWindow: ChartWindow? = null,
    val enablePanning: Boolean = true,
) : ChartProperties
