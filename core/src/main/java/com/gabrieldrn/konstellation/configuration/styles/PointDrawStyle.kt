package com.gabrieldrn.konstellation.configuration.styles

import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

/**
 * Defines drawing style for points (circles) into charts.
 *
 * @param color Point's circle color.
 * @param radius Point's circle radius.
 */
data class PointDrawStyle(
    override var color: Color = Color.Black,
    var radius: Dp = 3.dp
) : DrawStyle
