package com.gabrieldrn.konstellation.configuration.styles

import androidx.compose.ui.graphics.Color

/**
 * Data class defining the appearance of an axis.
 *
 * @property axisLineStyle Axis' line appearance.
 * @property tickLineStyle Appearance of ticks drawn on the axis.
 * @property tickLineStyle Appearance of ticks text labels.
 */
data class AxisDrawStyle(
    var axisLineStyle: LineDrawStyle = LineDrawStyle(),
    var tickLineStyle: LineDrawStyle = LineDrawStyle(),
    var tickTextStyle: TextDrawStyle = TextDrawStyle(),
)

/**
 * Applies given [color] to all sub-styles.
 */
fun AxisDrawStyle.setColor(color: Color) {
    axisLineStyle.color = color
    tickLineStyle.color = color
    tickTextStyle.color = color
}
