package com.gabrieldrn.konstellation.math

import androidx.compose.ui.geometry.*
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.plotting.offsets
import org.junit.Test
import kotlin.test.assertEquals

class NumbersMappingTest {

    @Test
    fun numbersMapping_map_mapsCorrectly() {
        val testCandidates = listOf(
            10f map (0f..20f to 0f..40f) to 20f,
            250f map (0f..500f to 0f..1f) to .5f,
            0f map (0f..1f to 0f..100f) to 0f,
            1f map (0f..1f to 0f..100f) to 100f,
            // signed values
            -1f map (-1f..1f to 0f..100f) to 0f,
            0f map (-1f..1f to 0f..100f) to 50f,
            // out of bounds
            -5f map (0f..5f to 0f..100f) to -100f,
            -2.5f map (0f..5f to 0f..100f) to -50f,
        )

        testCandidates.forEach {
            assertEquals(
                expected = it.second,
                actual = it.first
            )
        }
    }

    @Test
    fun numbersMapping_createOffsets_createsOffsetsCorrectly() {
        val dataset = datasetOf(
            -3f by -1f,
            -2f by 0f,
            -1f by 0f,
            0f by 2f,
            1f by 2f,
            2f by 1f,
            3f by 1f,
        )
        val actual = dataset.createOffsets(
            size = Size(1048f, 1048f),
            xWindowRange = -3f..3f,
            yWindowRange = -6f..7f
        ).offsets

        val expected = listOf(
            Offset(0f, 644.9f),
            Offset(174.7f, 564.3f),
            Offset(349.3f, 564.3f),
            Offset(524f, 403.1f),
            Offset(698.7f, 403.1f),
            Offset(873.3f, 483.7f),
            Offset(1048f, 483.7f),
        )

        expected.zip(actual).forEachIndexed { index, offsets ->
            assertEquals(
                expected = offsets.first.x,
                actual = offsets.second.x,
                absoluteTolerance = 0.1f,
                message = "At index $index,"
            )
            assertEquals(
                expected = offsets.first.y,
                actual = offsets.second.y,
                absoluteTolerance = 0.1f,
                message = "At index $index,"
            )
        }
    }
}
