package dev.gabrieldrn.konstellation.drawing

import android.graphics.Paint
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnitType
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
                setTextSize(style, this@drawText)
                color = style.color.toInt()
                typeface = style.typeface
                flags = Paint.ANTI_ALIAS_FLAG
            }
        )
    }
}

/**
 * Considering this Paint as a text paint, applies the size defined in [textStyle] to it.
 */
public fun Paint.setTextSize(textStyle: TextDrawStyle, density: Density): Paint = apply {
    when (textStyle.textSize.type) {
        TextUnitType.Em -> textSize *= textStyle.textSize.value
        TextUnitType.Sp -> textSize = with(density) { textStyle.textSize.toPx() }
        else -> {}
    }
}
