package com.gabrieldrn.konstellation.configuration.styles

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

/**
 * Defines drawing style for texts into charts.
 *
 * @param color Text color.
 * @param typeface See [Typeface].
 * @param textSize Size of text. TODO Use a dimension instead of Float
 * @param textAlign Text alignment. See [Paint.Align]
 * @param offsetX An offset applied to the X-position of the text.
 * @param offsetY An offset applied to the Y-position of the text.
 */
data class TextDrawStyle(
    override var color: Color = Color.Black,
    var typeface: Typeface = Typeface.DEFAULT,
    var textSize: TextUnit = 32.sp,
    var textAlign: Paint.Align = Paint.Align.LEFT,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
) : DrawStyle
