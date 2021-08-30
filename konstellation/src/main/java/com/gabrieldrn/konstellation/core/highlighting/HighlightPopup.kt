package com.gabrieldrn.konstellation.core.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.core.plotting.Point

/**
 * A scope providing placement and contents data for a highlighting popup.
 * @param point Highlighted [Point].
 * @param position Where the popup will be gravitating in front of the chart.
 */
class HighlightPopupScope(
    val point: Point,
    val position: HighlightPosition,
    private val chartPaddings: PaddingValues
) {
    internal var paddingTop = 0
    internal var paddingStart = 0

    val popupPositioner: Density.() -> IntOffset = {
        when (position) {
            HighlightPosition.TOP, HighlightPosition.POINT -> {
                IntOffset(
                    point.xPos.toInt() + paddingStart,
                    point.yPos.toInt() + paddingTop
                )
            }
            HighlightPosition.BOTTOM -> IntOffset(point.xPos.toInt() + paddingStart, 0)
            HighlightPosition.START -> IntOffset(0, point.yPos.toInt())
            HighlightPosition.END -> IntOffset(0, point.yPos.toInt())
        }
    }

    @Composable
    internal fun ComputePaddings() {
        LocalDensity.current.run {
            paddingTop = chartPaddings.calculateTopPadding().toPx().toInt()
            paddingStart = chartPaddings.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
        }
    }
}

/**
 * Creates and places a [Box] ready to compose a highlight popup within a highlighting [scope] with
 * a given [content] provided by the user.
 */
@Composable
internal fun BoxScope.BoxedPopup(
    scope: HighlightPopupScope,
    content: @Composable HighlightPopupScope.(Point) -> Unit
) {
    fun getPlacementOffset(p: Placeable) = when (scope.position) {
        HighlightPosition.TOP ->
            -IntOffset(p.width / 2, scope.point.yPos.toInt() + scope.paddingTop)
        HighlightPosition.BOTTOM ->
            -IntOffset(p.width / 2, 0)
        HighlightPosition.START, HighlightPosition.END ->
            -IntOffset(0, p.height / 2) + IntOffset(0, scope.paddingTop)
        HighlightPosition.POINT ->
            -IntOffset(p.width / 2, p.height)
    }

    val popupLayoutModifier: MeasureScope.(Measurable, Constraints) -> MeasureResult = { m, c ->
        val placeable = m.measure(c)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(getPlacementOffset(placeable))
        }
    }

    fun getAlignment() = when (scope.position) {
        HighlightPosition.BOTTOM -> Alignment.BottomStart
        HighlightPosition.END -> Alignment.TopEnd
        else -> Alignment.TopStart
    }

    Box(
        Modifier
            .offset(scope.popupPositioner)
            .align(getAlignment())
            .layout(popupLayoutModifier)
    ) {
        content(scope, scope.point)
    }
}

/**
 * Creates a popup to highlight content from the chart with a [shape]. The default shape, a
 * [HighlightPopupShape], is a rounded card with an arrow placed in accordance with the positions
 * of the highlight defined in the chart composable parameters, so as it will pointing towards the
 * highlighted value. The background of this shape is customizable by modifying [backgroundColor].
 * Contents of the highlighting popup is defined in [content].
 */
@Composable
fun HighlightPopupScope.HighlightPopup(
    modifier: Modifier = Modifier,
    shape: HighlightPopupShape = HighlightPopupShape(position),
    backgroundColor: Color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
    content: @Composable BoxScope.(Point) -> Unit
) {
    Card(
        modifier
            .padding(shape.arrowSize)
            .sizeIn(
                minWidth = shape.suggestedMinWidth,
                minHeight = shape.suggestedMinHeight
            )
            .then(modifier),
        backgroundColor = backgroundColor,
        shape = shape,
        elevation = 4.dp
    ) {
        Box(content = { content(point) })
    }
}
