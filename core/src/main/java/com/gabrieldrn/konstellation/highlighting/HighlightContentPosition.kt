package com.gabrieldrn.konstellation.highlighting

/**
 * Positions where a highlight popup can be placed.
 * [TOP], [BOTTOM], [START], [END] represents the positions where the popup will stick over the
 * chart "window". [POINT] will place the popup up above the highlighted point.
 */
enum class HighlightContentPosition {
    TOP, BOTTOM, START, END, POINT
}

/**
 * Array of the possible vertical positions where the popup can be placed.
 */
val verticalHLPositions = arrayOf(
    HighlightContentPosition.POINT,
    HighlightContentPosition.TOP,
    HighlightContentPosition.BOTTOM
)

/**
 * Array of the possible horizontal positions where the popup can be placed.
 */
val horizontalHLPositions = arrayOf(
    HighlightContentPosition.START,
    HighlightContentPosition.END
)

/**
 * Returns true if this position is a vertical one.
 */
val HighlightContentPosition.isVertical
    get() = this in verticalHLPositions

/**
 * Returns true if this position is a horizontal one.
 */
val HighlightContentPosition.isHorizontal
    get() = this in horizontalHLPositions
