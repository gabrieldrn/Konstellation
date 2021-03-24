package com.gabrieldrn.konstellation.core.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.gabrieldrn.konstellation.core.plotting.Point
import com.gabrieldrn.konstellation.core.plotting.xMax
import com.gabrieldrn.konstellation.core.plotting.yRange
import kotlin.math.abs

/**
 * Given two ranges A and B and a point at a position P1 inside A, this function will, from B,
 * return a position P2 which value is relative to the value of P1.
 *
 * Example:
 *
 * ```
 *  0.0    P1 = 250     500.0
 * A|---------x---------|
 * B|---------x---------|
 *  0.0    P2 = 0.5     1.0
 * ```
 *
 * Given a range A [[0.0; 500.0]] and a range B [[0.0;1.0]], if P1 = 250 then P2 = 0.5
 */
internal fun convertPointRanges(
    initRange: ClosedRange<Float>,
    targetRange: ClosedRange<Float>,
    initPoint: Float,
): Float {
    require(initPoint in initRange) {
        "Initial point must be within the bounds of the initial range"
    }
    val offsetRange = if (targetRange.start < 0.0) abs(targetRange.start) else 0f
    return (((targetRange.endInclusive + offsetRange) - (targetRange.start + offsetRange))
            * (initPoint - initRange.start) / (initRange.endInclusive - initRange.start)
            ) - offsetRange
}

/**
 * TODO FIX KDOC
 * Converts a collection of [Point] to a list of [Offset]s placed relatively by the current canvas
 * dimensions.
 */
internal fun Collection<Point>.createOffsets(drawScope: DrawScope, yRange: ClosedFloatingPointRange<Float>): Collection<Point> {
    val canvasYRange = drawScope.size.height..0f //Inversion de l'axe Y (norme informatique).
    //Les attributs d'extention de Collection<Point> peuvent peut-être impacter la vitesse de
    //calcul. A vérifier.
    return map {
        it.copy(
            y = -it.y,
            offset = Offset(
                x = (it.x / this.xMax) * drawScope.size.width,  //TODO Ne Fonctionne pour LinePlotter
                y = convertPointRanges(yRange, canvasYRange, it.y) + drawScope.size.height,
//            y = ((yMax - it.y) * size.height) / yMax,
            )
        )
    }
}

/**
 * Returns a position inside the given data range, relative to a given X position in the canvas.
 *
 * Example:
 *
 * Given a canvas width of 500 and data set with a range of [0.0;1.0]
 * If canvas x pos = 250
 * Then returned pos in dataset = 0.5
 *
 * @param canvasPos X position in the current canvas.
 * @param range Range of the dataset
 */
internal fun DrawScope.convertCanvasXToDataX(
    canvasPos: Int,
    range: ClosedFloatingPointRange<Float>
) = convertPointRanges(0f..size.width, range, canvasPos.toFloat())
