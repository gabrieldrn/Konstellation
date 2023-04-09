package com.gabrieldrn.konstellation.configuration.properties

/**
 * This class details offsets to apply around a dataset, usually to create a padding effect around
 * it and avoid having points drawn on axes.
 */
data class DatasetOffsets(

    /**
     * Number of units to be added before the lowest point of the dataset on the X axis.
     */
    val xStartOffset: Float? = null,

    /**
     * Number of units to be added after the highest point of the dataset on the X axis.
     */
    val xEndOffset: Float? = null,

    /**
     * Number of units to be added before the lowest point of the dataset on the Y axis.
     */
    val yStartOffset: Float? = null,

    /**
     * Number of units to be added after the highest point of the dataset on the Y axis.
     */
    val yEndOffset: Float? = null,
)
