package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color

data class LineDrawStyle(
    val color: Color = Color.Black,
    val strokeWidth: Float = 5f,
) : DrawStyle
