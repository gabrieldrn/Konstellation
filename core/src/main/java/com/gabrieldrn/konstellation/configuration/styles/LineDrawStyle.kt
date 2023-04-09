package com.gabrieldrn.konstellation.configuration.styles

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    override val color: Color = Color.Black,
    val strokeWidth: Dp = 1.5.dp,
    val cap: StrokeCap = StrokeCap.Square,
    val dashed: Boolean = false,
    val dashedEffectIntervals: FloatArray = floatArrayOf(
        defaultHighlightDashedLineOn,
        defaultHighlightDashedLineOff
    ),
    val dashedEffectPhase: Float = 0f,
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
        private const val defaultHighlightDashedLineOn = 10f
        private const val defaultHighlightDashedLineOff = 20f
    }
}
