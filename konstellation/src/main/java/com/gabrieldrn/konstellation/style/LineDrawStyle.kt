package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LineDrawStyle(
    val color: Color = Color.Black,
    val strokeWidth: Dp = 1.5f.dp,
    val cap: StrokeCap = StrokeCap.Round
) : DrawStyle
