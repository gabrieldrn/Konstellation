package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.gabrieldrn.konstellation.style.AxisDrawStyle
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.NiceScale
import com.gabrieldrn.konstellation.util.rawRange
import com.gabrieldrn.konstellation.util.toInt

internal fun DrawScope.drawScaledXAxis(
    xRange: ClosedFloatingPointRange<Float>,
    axisDrawStyle: AxisDrawStyle = AxisDrawStyle()
) {
    //Axis scale computation
    val scaleCalc = NiceScale(xRange.start, xRange.endInclusive, 10)

    //Initial drawing point : bottom left
    var zo = Offset(0f, size.height)

    //Axis line
    drawLine(zo, Offset(size.width, size.height), axisDrawStyle.axisLineStyle)

    //Space between left canvas border and left chart "window" depending on chart values
    val leftOffset = (size.width * (xRange.start - scaleCalc.niceMin)) / xRange.rawRange
    //Space between right chart "window" and right canvas border depending on chart values
    val rightOffset = (size.width * (scaleCalc.niceMax - xRange.endInclusive)) / xRange.rawRange
    //Number of ticks
    val tickCount = (scaleCalc.niceMax - scaleCalc.niceMin) / scaleCalc.tickSpacing + 1
    //Space between each tick
    val tickSpacingOffset = Offset((size.width + leftOffset + rightOffset) / (tickCount - 1), 0f)
    //Potential shift of the initial drawing point on the left if offset != 0
    zo -= Offset(leftOffset, 0f)
    //First tick label value
    var tickValue = scaleCalc.niceMin

    //Labels drawing
    while (tickValue <= scaleCalc.niceMax) {
        if (tickValue in xRange) {
            drawTick(zo, axisDrawStyle.tickLineStyle)
            drawTickLabel(Offset(zo.x, zo.y), tickValue.toString(), axisDrawStyle.tickTextStyle)
        }
        zo += tickSpacingOffset
        tickValue += scaleCalc.tickSpacing
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
