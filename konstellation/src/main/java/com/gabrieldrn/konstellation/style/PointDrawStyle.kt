package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines drawing style for points (circles) into charts.
 *
 * @param color Point's circle color.
 * @param radius Point's circle radius.
 */
data class PointDrawStyle(
    override val color: Color = Color.Black,
    val radius: Dp = 3.dp
) : DrawStyle
