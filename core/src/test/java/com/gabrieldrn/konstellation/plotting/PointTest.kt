package com.gabrieldrn.konstellation.plotting

import androidx.compose.ui.geometry.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PointTest {

    @Test fun point_xyOffsetPosGetters_returnsCorrectValues() {
        val point = Point(0f, 0f).apply { offset = Offset(1f, 2f) }
        assertEquals(
            expected = 1f,
            actual = point.xPos
        )
        assertEquals(
            expected = 2f,
            actual = point.yPos
        )
    }

    @Test fun point_byInfix_instantiatesPointCorrectly() {
        val x = 1f
        val y = 2f
        val result = x by y

        assertIs<Point>(
            value = result,
            message = "The 'by' infix function should return an instance of Point."
        )
        assertEquals(
            expected = x,
            actual = result.x,
            message = "The x value of the created Point should be the receiver of the 'by' infix function."
        )
        assertEquals(
            expected = y,
            actual = result.y,
            message = "The y value of the created Point should be the parameter of the 'by' infix function."
        )
    }
}
