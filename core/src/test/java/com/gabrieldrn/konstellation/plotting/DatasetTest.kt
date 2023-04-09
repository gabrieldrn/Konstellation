package com.gabrieldrn.konstellation.plotting

import androidx.compose.ui.geometry.Offset
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DatasetTest {

    private val baseDataset = datasetOf(
        0f by 0f,
        25f by 50f,
        50f by 200f,
        75f by 150f,
        100f by 175f
    )

    @Test fun dataset_datasetOf_createsDataset() {
        val dataset = baseDataset
        val emptyDataset = datasetOf()

        assertIs<Dataset>(value = dataset)
        assertEquals(
            expected = 5,
            actual = dataset.size
        )
        assertIs<Dataset>(value = emptyDataset)
        assert(emptyDataset.isEmpty())
    }

    @Test fun dataset_listOfPoints_asDataset_castsDataset() {
        val dataset = listOf(
            0f by 0f,
            25f by 50f,
            50f by 200f,
            75f by 150f,
            100f by 175f
        ).asDataset()

        assertIs<Dataset>(value = dataset)
    }

    @Test fun dataset_collectionOfFloatPairs_toDataset_mappedAsDataset() {
        val setOfPoints = setOf(
            0f to 0f,
            25f to 50f,
            50f to 200f,
            75f to 150f,
            100f to 175f
        ).toDataset()
        val listOfPoints = listOf(
            0f to 0f,
            25f to 50f,
            50f to 200f,
            75f to 150f,
            100f to 175f
        ).toDataset()

        assertIs<Dataset>(value = setOfPoints)
        assertIs<Dataset>(value = listOfPoints)
    }

    @Test fun dataset_getOffsets_returnsListOfOffsets() {
        val offsets = baseDataset.offsets

        assertIs<List<Offset>>(offsets)
    }

    @Test fun dataset_nearestPointByX_returnsCorrectValue() {
        val dataset = baseDataset.map {
            it.copy(offset = Offset(it.x, it.y))
        }
        dataset.forEachIndexed { index, _ ->
            assertEquals(
                expected = dataset[index],
                actual = dataset.nearestPointByX(dataset[index].x)
            )
        }
        assertEquals(
            expected = dataset[1],
            actual = dataset.nearestPointByX(37.5f)
        )
        assertEquals(
            expected = dataset.first(),
            actual = dataset.nearestPointByX(-200f)
        )
        assertEquals(
            expected = dataset.last(),
            actual = dataset.nearestPointByX(200f)
        )
    }

    @Test fun dataset_xyMinMaxGetters_returnsCorrectValues() {
        val dataset = baseDataset

        assertEquals(
            expected = dataset.first().x,
            actual = dataset.xMin
        )
        assertEquals(
            expected = dataset.last().x,
            actual = dataset.xMax
        )
        assertEquals(
            expected = dataset.first().y,
            actual = dataset.yMin
        )
        assertEquals(
            expected = dataset[2].y,
            actual = dataset.yMax
        )
    }

    @Test fun dataset_xyRangesGetters_returnsCorrectValues() {
        val dataset = baseDataset

        assertEquals(
            expected = dataset.first().x..dataset.last().x,
            actual = dataset.xRange
        )
        assertEquals(
            expected = dataset.first().y..dataset[2].y,
            actual = dataset.yRange
        )
    }
}
