package com.gabrieldrn.konstellation.core.highlighting

/**
 * Positions at where the highlight popup can be placed.
 * [TOP], [BOTTOM], [START], [END] represents the positions where the popup will stick over chart
 * "window". [POINT] will place the popup up above the highlighted point.
 */
enum class HighlightPosition {
    TOP, BOTTOM, START, END, POINT
}

/**
 * Array of the possible vertical positions where the popup can be placed.
 */
internal val verticalHLPositions
    get() = arrayOf(
        HighlightPosition.POINT, HighlightPosition.TOP, HighlightPosition.BOTTOM
    )

/**
 * Array of the possible horizontal positions where the popup can be placed.
 */
internal val horizontalHLPositions
    get() = arrayOf(
        HighlightPosition.START, HighlightPosition.END
    )

/**
 * Returns true if this position is a vertical one.
 */
internal val HighlightPosition.isVertical
    get() = this in verticalHLPositions

/**
 * Returns true if this position is a horizontal one.
 */
internal val HighlightPosition.isHorizontal
    get() = this in horizontalHLPositions
