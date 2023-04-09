package com.gabrieldrn.konstellation.configuration.styles

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Defines drawing style for texts into charts.
 *
 * @property color Text color.
 * @property typeface See [Typeface].
 * @property textSize Size of text.
 * @property textAlign Text alignment. See [Paint.Align]
 * @property offsetX An offset applied to the X-position of the text.
 * @property offsetY An offset applied to the Y-position of the text.
 */
data class TextDrawStyle(
    override val color: Color = Color.Black,
    val typeface: Typeface = Typeface.DEFAULT,
    val textSize: TextUnit = 32.sp,
    val textAlign: Paint.Align = Paint.Align.LEFT,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
) : DrawStyle
