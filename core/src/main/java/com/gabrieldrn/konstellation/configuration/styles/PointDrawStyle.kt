package com.gabrieldrn.konstellation.configuration.styles

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines drawing style for points (circles) into charts.
 *
 * @property color Point's circle color.
 * @property radius Point's circle radius.
 */
public data class PointDrawStyle(
    override val color: Color = Color.Black,
    val radius: Dp = 3.dp
) : DrawStyle
