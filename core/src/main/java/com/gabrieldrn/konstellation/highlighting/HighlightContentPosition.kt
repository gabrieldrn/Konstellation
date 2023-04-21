package com.gabrieldrn.konstellation.highlighting

/**
 * Positions where a highlight popup can be placed.
 * [Top], [Bottom], [Start], [End] represents the positions where the popup will stick over the
 * chart "window". [Point] will place the popup up above the highlighted point.
 */
public enum class HighlightContentPosition {
    Top, Bottom, Start, End, Point
}

/**
 * Array of the possible vertical positions where the popup can be placed.
 */
public val verticalHLPositions: List<HighlightContentPosition> = listOf(
    HighlightContentPosition.Point,
    HighlightContentPosition.Top,
    HighlightContentPosition.Bottom
)

/**
 * Array of the possible horizontal positions where the popup can be placed.
 */
public val horizontalHLPositions: List<HighlightContentPosition> = listOf(
    HighlightContentPosition.Start,
    HighlightContentPosition.End
)

/**
 * Returns true if this position is a vertical one.
 */
public val HighlightContentPosition.isVertical: Boolean
    get() = this in verticalHLPositions

/**
 * Returns true if this position is a horizontal one.
 */
public val HighlightContentPosition.isHorizontal: Boolean
    get() = this in horizontalHLPositions
