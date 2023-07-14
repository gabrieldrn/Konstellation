package dev.gabrieldrn.konstellation.charts.line.limitline

import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.plotting.Axis

/**
 * Represents a limit line to be drawn on a Line chart. Limit lines are simple lines that can be
 * drawn on a chart to indicate a certain value. They can be drawn on any axis, and can have a label
 * next to them.
 *
 * @property value Value of the limit line.
 * @property axis Axis on which the limit line will be drawn. This affects the position of the
 * label.
 * @property style Style of the limit line.
// * @property label Label to be drawn next to the limit line, on the axis specified by [axis].
 */
public data class LimitLine(
    val value: Float,
    val axis: Axis,
    val style: LineDrawStyle = LineDrawStyle(),
//    val label: String? = null, TODO
)
