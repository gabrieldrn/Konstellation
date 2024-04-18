package dev.gabrieldrn.konstellation.highlighting

/**
 * Base definition of combined highlight properties and style that can be applied to a chart.
 */
public interface HighlightConfig {

    /**
     * If true, any highlight change shall perform a haptic feedback.
     */
    public val enableHapticFeedback: Boolean
}
