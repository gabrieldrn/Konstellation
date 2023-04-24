package com.gabrieldrn.konstellation.charts.line.configuration

import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.xRange
import com.gabrieldrn.konstellation.plotting.yRange
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.ObjectStreamException
import java.io.Serializable

/**
 * Defines a window of visualization for the chart. The values of the window are expressed in
 * units of the chart. For example, if the window is defined as being from 0 to 10, the
 * chart will display the values between 0 and 10.
 * @property xWindow Visualization window on the x-axis.
 * @property yWindow Visualization window on the y-axis.
 */
public data class ChartWindow(
    val xWindow: ClosedFloatingPointRange<Float>,
    val yWindow: ClosedFloatingPointRange<Float>,
): Serializable {

    @Suppress("unused")
    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeFloat(xWindow.start)
        out.writeFloat(xWindow.endInclusive)
        out.writeFloat(yWindow.start)
        out.writeFloat(yWindow.endInclusive)
    }

    @Suppress("unused", "FunctionParameterNaming")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        val xStart = `in`.readFloat()
        val xEnd = `in`.readFloat()
        val yStart = `in`.readFloat()
        val yEnd = `in`.readFloat()
        ChartWindow(
            xWindow = xStart..xEnd,
            yWindow = yStart..yEnd,
        )
    }

    @Suppress("unused")
    @Throws(ObjectStreamException::class)
    private fun readObjectNoData() {
        ChartWindow(
            xWindow = 0f..0f,
            yWindow = 0f..0f,
        )
    }

    public companion object {
        private const val serialVersionUID = 1L

        /**
         * Creates a new ChartWindow based on the given dataset. The window will be defined as the
         * range of the dataset.
         */
        public fun fromDataset(dataset: Dataset): ChartWindow {
            return ChartWindow(
                xWindow = dataset.xRange,
                yWindow = dataset.yRange
            )
        }
    }
}
