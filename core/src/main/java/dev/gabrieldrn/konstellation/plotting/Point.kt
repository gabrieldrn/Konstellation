package dev.gabrieldrn.konstellation.plotting

import androidx.compose.ui.geometry.*
import java.io.Serializable

/**
 * A class representing a point in a chart.
 */
public data class Point constructor(

    /**
     * X point value from the data set.
     */
    val x: Float,

    /**
     * Y point value from the data set.
     */
    val y: Float,

    /**
     * Offset corresponding to the position of the point into a chart.
     */
    val offset: Offset = Offset(0f, 0f),

    ): Serializable {

    /**
     * Returns the x value of [offset].
     */
    public val xPos: Float get() = offset.x

    /**
     * Returns the y value of [offset].
     */
    public val yPos: Float get() = offset.y

    override fun toString(): String = "[($x;$y), $offset]"

    public companion object {
        /** Required by [Serializable]. */
        private const val serialVersionUID = 1L
    }
}
