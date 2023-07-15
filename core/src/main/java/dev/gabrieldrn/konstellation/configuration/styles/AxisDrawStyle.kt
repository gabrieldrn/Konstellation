package dev.gabrieldrn.konstellation.configuration.styles

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color

/**
 * Data class defining the appearance of an axis.
 *
 * @property axisLineStyle Axis line appearance.
 * @property tickLineStyle Appearance of ticks drawn on the axis.
 * @property tickLabelStyle Appearance of ticks text labels.
 */
public data class AxisDrawStyle(
    val axisLineStyle: LineDrawStyle = LineDrawStyle(),
    val tickLineStyle: LineDrawStyle = LineDrawStyle(),
    val tickLabelStyle: TextDrawStyle = TextDrawStyle(),
)

/**
 * Applies given [color] to all sub-styles.
 */
public fun AxisDrawStyle.updateColor(color: Color): AxisDrawStyle = copy(
    axisLineStyle = axisLineStyle.copy(color = color),
    tickLineStyle = tickLineStyle.copy(color = color),
    tickLabelStyle = tickLabelStyle.copy(color = color),
)

/**
 * Applies given [typeface] to the text of ticks labels.
 */
public fun AxisDrawStyle.updateTypeface(typeface: Typeface): AxisDrawStyle = copy(
    tickLabelStyle = tickLabelStyle.copy(typeface = typeface),
)
