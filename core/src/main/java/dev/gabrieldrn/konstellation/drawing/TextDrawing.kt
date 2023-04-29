package dev.gabrieldrn.konstellation.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import dev.gabrieldrn.konstellation.util.toInt

/**
 * Draws a text into the current DrawScope.
 */
public fun DrawScope.drawText(
    point: Offset,
    text: String,
    style: TextDrawStyle = TextDrawStyle()
) {
    drawIntoCanvas {
        it.nativeCanvas.drawText(
            text,
            point.x + style.offsetX,
            point.y + style.offsetY,
            Paint().apply {
                textAlign = style.textAlign
                textSize = style.textSize.toPx()
                color = style.color.toInt()
                typeface = style.typeface
                flags = Paint.ANTI_ALIAS_FLAG
            }
        )
    }
}
