package com.gabrieldrn.konstellation.style

/**
 * This class details offsets to apply around a dataset, usually to create a padding effect around
 * it and avoid having points drawn on axes.
 */
data class DatasetOffsets(
    val xStartOffset: Float? = null,
    val xEndOffset: Float? = null,
    val yStartOffset: Float? = null,
    val yEndOffset: Float? = null,
)
