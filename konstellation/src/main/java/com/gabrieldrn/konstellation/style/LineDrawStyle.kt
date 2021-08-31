package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines drawing style for lines into charts.
 *
 * @param color Line color.
 * @param strokeWidth Line thickness.
 * @param cap Line ending style. See [StrokeCap].
 */
data class LineDrawStyle(
    override var color: Color = DEFAULT_LINE_COLOR,
    var strokeWidth: Dp = DEFAULT_STROKE_WIDTH,
    var cap: StrokeCap = DEFAULT_LINE_CAP,
    var dashed: Boolean = DEFAULT_DASHED_ENABLED,
    var dashedEffectIntervals: FloatArray = floatArrayOf(
        DEFAULT_HIGHLIGHT_DASHED_LINE_ON,
        DEFAULT_HIGHLIGHT_DASHED_LINE_OFF
    ),
    var dashedEffectPhase: Float = DEFAULT_HIGHLIGHT_DASHED_LINE_PHASE,
) : DrawStyle {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LineDrawStyle

        return !(color != other.color
                || strokeWidth != other.strokeWidth
                || cap != other.cap
                || dashed != other.dashed
                || dashedEffectIntervals.contentEquals(other.dashedEffectIntervals)
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
        private val DEFAULT_LINE_COLOR = Color.Black
        private val DEFAULT_STROKE_WIDTH = 1.5.dp
        private val DEFAULT_LINE_CAP = StrokeCap.Round
        private const val DEFAULT_DASHED_ENABLED = false
        private const val DEFAULT_HIGHLIGHT_DASHED_LINE_ON = 10f
        private const val DEFAULT_HIGHLIGHT_DASHED_LINE_OFF = 20f
        private const val DEFAULT_HIGHLIGHT_DASHED_LINE_PHASE = 0f
    }
}
