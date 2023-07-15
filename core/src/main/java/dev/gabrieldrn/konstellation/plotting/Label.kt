package dev.gabrieldrn.konstellation.plotting

import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle

/**
 * Represents a label that can be drawn on a chart.
 *
 * @property text Text to be drawn.
 * @property style Style of the text.
 */
public data class Label(
    val text: String,
    val style: TextDrawStyle,
)
