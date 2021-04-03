package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines drawing style for lines into charts.
 *
 * @param color Line color.
 * @param strokeWidth Line thickness.
 * @param cap Line ending style. See [StrokeCap].
 */
data class LineDrawStyle(
    override val color: Color = Color.Black,
    val strokeWidth: Dp = 1.5f.dp,
    val cap: StrokeCap = StrokeCap.Round
) : DrawStyle
