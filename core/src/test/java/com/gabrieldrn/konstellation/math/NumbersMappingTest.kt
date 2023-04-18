package com.gabrieldrn.konstellation.math

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class NumbersMappingTest {

    @Test
    fun numbersMapping_map_mapsCorrectly() {
        val testCandidates = listOf(
            10f map (0f..20f to 0f..40f) to 20f,
            250f map (0f..500f to 0f..1f) to .5f
        )

        testCandidates.forEach {
            assertEquals(
                expected = it.second,
                actual = it.first
            )
        }
    }

    @Test
    fun numbersMapping_map_failsOnOutOfBoundsValue() {
        assertFails {
            10f map (20f..30f to 30f..40f)
        }
    }
}
