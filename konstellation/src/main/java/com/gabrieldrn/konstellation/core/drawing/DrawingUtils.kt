package com.gabrieldrn.konstellation.core.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.toInt

/**
 * Draws a text into the current DrawScope.
 */
internal fun DrawScope.drawText(
    point: Offset,
    text: String,
    style: TextDrawStyle = TextDrawStyle()
) = drawIntoCanvas {
    it.nativeCanvas.drawText(
        text,
        point.x + style.offsetX,
        point.y + style.offsetY,
        Paint().apply {
            textAlign = style.textAlign
            textSize = style.textSize
            color = style.color.toInt()
            typeface = style.typeface
            flags = Paint.ANTI_ALIAS_FLAG
        }
    )
}
