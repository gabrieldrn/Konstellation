package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.gabrieldrn.konstellation.core.data.getTickDivision
import com.gabrieldrn.konstellation.style.AxisDrawStyle
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.toInt

internal fun DrawScope.drawScaledXAxis(
    xRange: ClosedFloatingPointRange<Float>,
    axisDrawStyle: AxisDrawStyle = AxisDrawStyle(),
    tickCount: Int = 5
) {
    var zo = Offset(0f, size.height)
    drawLine(zo, Offset(size.width, size.height), axisDrawStyle.axisLineStyle)
    val tickStep = xRange.getTickDivision(tickCount + 1)
    var tickValue = xRange.start
    val tickOffset = Offset(size.width / tickCount, 0f)
    repeat(tickCount + 1) {
        drawTick(zo, axisDrawStyle.tickLineStyle)
        drawTickLabel(Offset(zo.x, zo.y), tickValue.toString(), axisDrawStyle.tickTextStyle)
        zo += tickOffset
        tickValue += tickStep
    }
}

/**
 * Draws a tiny vertical line representing a tick.
 */
internal fun DrawScope.drawTick(
    center: Offset,
    tickLineStyle: LineDrawStyle = LineDrawStyle()
) {
    drawLine(
        start = Offset(center.x, center.y - 10f),
        end = Offset(center.x, center.y + 10f),
        lineStyle = tickLineStyle.copy(cap = StrokeCap.Square)
    )
}

/**
 * Draws a text into the current DrawScope.
 */
internal fun DrawScope.drawTickLabel(
    point: Offset,
    text: String,
    style: TextDrawStyle = TextDrawStyle()
) = drawIntoCanvas {
    val paint = Paint().apply {
        textAlign = style.textAlign
        textSize = style.textSize
        color = style.color.toInt()
        typeface = style.typeface
        flags = Paint.ANTI_ALIAS_FLAG
    }
    it.nativeCanvas.drawText(
        text,
        point.x + style.offsetX,
        (point.y + style.offsetY) + (paint.fontMetrics.descent - paint.fontMetrics.ascent),
        paint
    )
}
