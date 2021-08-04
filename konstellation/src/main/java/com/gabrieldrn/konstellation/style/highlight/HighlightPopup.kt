package com.gabrieldrn.konstellation.style.highlight

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.core.plotting.Point

class HighlightPopupScope(
    val point: Point,
    val position: HighlightPosition,
    val chartPaddings: PaddingValues
) {
    internal var paddingTop = 0
    internal var paddingBottom = 0
    internal var paddingStart = 0
    internal var paddingEnd = 0

    @Composable
    internal fun ComputePaddings() {
        LocalDensity.current.run {
            paddingTop = chartPaddings.calculateTopPadding().toPx().toInt()
            paddingBottom = chartPaddings.calculateBottomPadding().toPx().toInt()
            paddingStart = chartPaddings.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
            paddingEnd = chartPaddings.calculateEndPadding(LayoutDirection.Ltr).toPx().toInt()
        }
    }
}

@Composable
internal fun BoxScope.HighlightPopup(
    scope: HighlightPopupScope,
    content: @Composable HighlightPopupScope.(Point) -> Unit
) {
    fun getPlacementOffset(p: Placeable) = when (scope.position) {
        HighlightPosition.TOP ->
            -IntOffset(p.width / 2, scope.point.offset.y.toInt() + scope.paddingTop)
        HighlightPosition.BOTTOM ->
            -IntOffset(p.width / 2, 0)
        HighlightPosition.POINT ->
            -IntOffset(p.width / 2, p.height)
        else -> IntOffset(0, 0) //TODO Implement placement of other positions
    }

    val popupLayoutModifier: MeasureScope.(Measurable, Constraints) -> MeasureResult = { m, c ->
        val placeable = m.measure(c)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(getPlacementOffset(placeable))
        }
    }

    fun getAlignment() = when (scope.position) {
        HighlightPosition.BOTTOM -> Alignment.BottomStart
        else -> Alignment.TopStart
    }

    Box(Modifier.align(getAlignment()).layout(popupLayoutModifier)) {
        content(scope, scope.point)
    }
}

@Composable
fun HighlightPopupScope.RoundedCardHighlightPopup(
    modifier: Modifier = Modifier,
    cardContent: @Composable BoxScope.(Point) -> Unit
) {
    val popupShape = HighlightPopupShape(position)
    val popupPositioner: Density.() -> IntOffset = {
        when (position) {
            HighlightPosition.TOP, HighlightPosition.POINT -> {
                IntOffset(
                    point.xPos.toInt() + paddingStart,
                    point.yPos.toInt() + paddingTop
                ) + if (position == HighlightPosition.POINT) {
                    IntOffset(0, -popupShape.arrowSize.toPx().toInt())
                } else {
                    IntOffset(0, 0)
                }
            }
            HighlightPosition.BOTTOM -> IntOffset(point.xPos.toInt() + paddingStart, 0)
            else -> IntOffset(0, 0)
        }
    }

    Card(
        Modifier
            .offset(popupPositioner)
            .padding(if (position != HighlightPosition.POINT) 4.dp else 8.dp)
            .sizeIn(
                minWidth = popupShape.suggestedMinWidth,
                minHeight = popupShape.suggestedMinHeight
            )
            .then(modifier),
        backgroundColor = Color.White,
        shape = popupShape,
        elevation = 4.dp
    ) {
        Box(content = { cardContent(point) })
    }
}
