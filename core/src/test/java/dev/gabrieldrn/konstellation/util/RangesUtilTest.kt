package dev.gabrieldrn.konstellation.util

import org.junit.Test
import kotlin.test.assertEquals

class RangesUtilTest {

    @Test
    fun rangesUtil_zoomAround_noZoom_calculatesCorrectly() {
        val candidate = 5f..10f
        assertEquals(
            expected = candidate,
            actual = candidate.zoomAround(1f)
        )
    }

    @Test
    fun rangesUtil_zoomAround_increaseZoom_zoomsCorrectly() {
        val candidate = 5f..10f
        // Applying a 1.5x zoom factor should increase range distance by ~66.67%
        assertEquals(
            expected = 3.75f..11.25f,
            actual = candidate.zoomAround(1.5f)
        )
    }

    @Test
    fun rangesUtil_zoomAround_decreaseZoom_zoomsCorrectly() {
        val candidate = 5f..10f
        // Applying a 0.5x zoom factor should decrease range distance by ~50%
        assertEquals(
            expected = 6.25f..8.75f,
            actual = candidate.zoomAround(0.5f)
        )
    }
}
