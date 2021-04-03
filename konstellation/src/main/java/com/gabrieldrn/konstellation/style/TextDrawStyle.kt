package com.gabrieldrn.konstellation.style

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color

/**
 * Defines drawing style for texts into charts.
 *
 * @param color Text color.
 * @param typeface See [Typeface].
 */
data class TextDrawStyle(
    override var color: Color = Color.Black,
    var typeface: Typeface = Typeface.DEFAULT,
) : DrawStyle
