package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PointDrawStyle(
    override val color: Color = Color.Black,
    val radius: Dp = 3.dp
) : DrawStyle
