package dev.gabrieldrn.konstellation.charts.function

/**
 * Indicates that the annotated API is still under development and may change in the future.
 */
@RequiresOptIn(
    message = "This API is still under development and may change in the future.",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
public annotation class WipChartApi
