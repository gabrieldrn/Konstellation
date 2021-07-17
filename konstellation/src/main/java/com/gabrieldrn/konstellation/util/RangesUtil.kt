package com.gabrieldrn.konstellation.util

/**
 * Distance between the first and last value (end - start)
 */
val ClosedFloatingPointRange<Float>.rawRange get() = endInclusive - start
