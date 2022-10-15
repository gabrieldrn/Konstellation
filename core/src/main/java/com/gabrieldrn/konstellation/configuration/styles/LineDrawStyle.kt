package com.gabrieldrn.konstellation.configuration.styles

import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

/**
 * Defines drawing style for lines into charts.
 *
 * See [PathEffect.dashPathEffect] for more insight on the dash effect.
 *
 * @property color Line color.
 * @property strokeWidth Line thickness.
 * @property cap Line ending style. See [StrokeCap].
 * @property dashed Indicates if the line has to be dashed.
 * @property dashedEffectIntervals Array of "on" and "off" distances for the dashed line segments.
 * @property dashedEffectPhase Pixel offset into the intervals array
 */
data class LineDrawStyle(
    override var color: Color = DefaultLineColor,
    var strokeWidth: Dp = DefaultStrokeWidth,
    var cap: StrokeCap = DefaultLineCap,
    var dashed: Boolean = DefaultDashed,
    var dashedEffectIntervals: FloatArray = floatArrayOf(
        DefaultHighlightDashedLineOn,
        DefaultHighlightDashedLineOff
    ),
    var dashedEffectPhase: Float = DefaultHighlightDashedLinePhase,
) : DrawStyle {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LineDrawStyle

        return !(color != other.color
                || strokeWidth != other.strokeWidth
                || cap != other.cap
                || dashed != other.dashed
                || !dashedEffectIntervals.contentEquals(other.dashedEffectIntervals)
                || dashedEffectPhase != other.dashedEffectPhase)
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + strokeWidth.hashCode()
        result = 31 * result + cap.hashCode()
        result = 31 * result + dashed.hashCode()
        result = 31 * result + dashedEffectIntervals.contentHashCode()
        result = 31 * result + dashedEffectPhase.hashCode()
        return result
    }

    companion object {
        val DefaultLineColor = Color.Black
        val DefaultStrokeWidth = 1.5.dp
        val DefaultLineCap = StrokeCap.Square
        const val DefaultDashed = false
        const val DefaultHighlightDashedLineOn = 10f
        const val DefaultHighlightDashedLineOff = 20f
        const val DefaultHighlightDashedLinePhase = 0f
    }
}
