package dev.gabrieldrn.konstellation.charts.line.properties

import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.highlighting.HighlightLinesPlacement
import dev.gabrieldrn.konstellation.highlighting.HighlightConfig

/**
 * 🔎 Class defining the highlight configuration of a Konstellation line chart, affecting how the
 * highlight is drawn. It combines properties and styles.
 *
 * @property contentPositions Where to place highlighting popups. There will be as many
 * popups as there are positions.
 * @property linesPlacement Where highlight lines will be placed. See enum values for more
 * insight. A null value means not drawing any highlight line.
 * @property enableHapticFeedback Either to perform haptic feedbacks every time a new value is
 * highlighted.
 * @property pointStyle Appearance of the highlighted data point.
 * @property lineStyle Appearance of the lines drawn on the highlighted point. Their
 * orientation depends on the provided highlighting positions in the LineChart composable.
 */
public data class LineChartHighlightConfig(
    val contentPositions: Set<HighlightContentPosition> = setOf(
        HighlightContentPosition.Point
    ),
    val linesPlacement: HighlightLinesPlacement? = HighlightLinesPlacement.Relative,
    override val enableHapticFeedback: Boolean = false,
    val pointStyle: PointDrawStyle = PointDrawStyle(),
    val lineStyle: LineDrawStyle? = LineDrawStyle(strokeWidth = 1.dp, dashed = true),
) : HighlightConfig
