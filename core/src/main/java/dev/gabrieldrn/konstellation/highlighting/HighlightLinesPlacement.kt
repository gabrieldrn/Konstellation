package dev.gabrieldrn.konstellation.highlighting

/**
 * Placement of highlight lines, drawn above the whole chart and the highlighted point.
 * Check the documentation of each enumeration for their related logic.
 */
public enum class HighlightLinesPlacement {

    /**
     * The lines will correspond to the position of the highlighted content (popups). For example,
     * if the popup is of position "point" or "top", then the line will be vertical. If there is
     * two popups of positions "top" and "left", then there will be both a horizontal and a vertical
     * line.
     */
    Relative,

    /**
     * Horizontally-oriented line, independent of highlight content orientation.
     */
    Horizontal,

    /**
     * Vertically-oriented line, independent of highlight content orientation.
     */
    Vertical,

    /**
     * Both horizontal and vertical oriented lines.
     */
    Both
}
