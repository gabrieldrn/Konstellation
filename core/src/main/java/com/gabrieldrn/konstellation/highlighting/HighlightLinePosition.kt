package com.gabrieldrn.konstellation.highlighting

/**
 * Positions of highlight lines, drawn across the whole chart and above the highlighted point.
 * Check the documentation of each enumeration for their related logic.
 */
enum class HighlightLinePosition {

    /**
     * The lines will correspond to the position of the highlighted content (popups). For example,
     * if the popup is of position "point" or "top", then the line will be vertical. If there is
     * two popups of positions "top" and "left", then there will be both a horizontal and a vertical
     * line.
     */
    Relative,

    /**
     * Horizontally-oriented line, independent of highlight content orientation
     */
    Horizontal,

    /**
     * Vertically-oriented line, independent of highlight content orientation
     */
    Vertical,

    /**
     * Both horizontal and vertical oriented lines.
     */
    Both
}
