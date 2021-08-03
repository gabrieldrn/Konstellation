package com.gabrieldrn.konstellation.style.highlight

enum class HighlightPosition {
    TOP, BOTTOM, START, END, POINT
}

internal val HighlightPosition.isVertical
    get() = this in arrayOf(
        HighlightPosition.POINT, HighlightPosition.TOP, HighlightPosition.BOTTOM
    )
internal val HighlightPosition.isHorizontal
    get() = this in arrayOf(
        HighlightPosition.START, HighlightPosition.END
    )