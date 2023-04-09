package com.gabrieldrn.konstellation.configuration.styles

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color

/**
 * Data class defining the appearance of an axis.
 *
 * @property axisLineStyle Axis line appearance.
 * @property tickLineStyle Appearance of ticks drawn on the axis.
 * @property tickTextStyle Appearance of ticks text labels.
 */
data class AxisDrawStyle(
    val axisLineStyle: LineDrawStyle = LineDrawStyle(),
    val tickLineStyle: LineDrawStyle = LineDrawStyle(),
    val tickTextStyle: TextDrawStyle = TextDrawStyle(),
)

/**
 * Applies given [color] to all sub-styles.
 */
fun AxisDrawStyle.updateColor(color: Color) = copy(
    axisLineStyle = axisLineStyle.copy(color = color),
    tickLineStyle = tickLineStyle.copy(color = color),
    tickTextStyle = tickTextStyle.copy(color = color),
)

/**
 * Applies given [typeface] to the text of ticks labels.
 */
fun AxisDrawStyle.updateTypeface(typeface: Typeface) = copy(
    tickTextStyle = tickTextStyle.copy(typeface = typeface),
)
