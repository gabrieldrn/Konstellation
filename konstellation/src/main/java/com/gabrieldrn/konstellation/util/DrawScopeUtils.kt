package com.gabrieldrn.konstellation.util

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

fun DrawScope.drawFrame(color: Color = Color.Gray) {
    drawLine(color, Offset(0f, 0f), Offset(size.width, 0f))
    drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height))
    drawLine(color, Offset(0f, 0f), Offset(0f, size.height))
    drawLine(color, Offset(0f, size.height), Offset(size.width, size.height))
}

/**
 * Draws a horizontal line at the middle of the canvas.
 */
fun DrawScope.drawMiddleHorizontalLine(color: Color = Color.Gray) =
    drawLine(color, Offset(0f, size.height / 2), Offset(size.width - 1, size.height / 2))

/**
 * Draws a vertical line at the middle of the canvas.
 */
fun DrawScope.drawMiddleVerticalLine(color: Color = Color.Gray) =
    drawLine(color, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height - 1))

/**
 * Draws a text into the current DrawScope
 */
fun DrawScope.drawText(
    point: Offset,
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    text: String,
    textAlign: Paint.Align = Paint.Align.CENTER,
    textSize: Float = 32f,
    typeface: Typeface,
    color: Int = android.graphics.Color.GRAY
) {
    drawIntoCanvas {
        it.nativeCanvas.drawText(
            text,
            point.x + offsetX,
            point.y + offsetY,
            Paint().apply {
                this.textAlign = textAlign
                this.textSize = textSize
                this.color = color
                this.typeface = typeface
                flags = Paint.ANTI_ALIAS_FLAG
            }
        )
    }
}
