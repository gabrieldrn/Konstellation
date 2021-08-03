package com.gabrieldrn.konstellation.style.highlight

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
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
internal fun HighlightPopupScope.HighlightPopup(
    content: @Composable HighlightPopupScope.(Point) -> Unit
) {
    LocalDensity.current.run { chartPaddings.calculateTopPadding().toPx() }.toInt()
    fun getPlacementOffset(p: Placeable) = when (position) {
        HighlightPosition.TOP -> -IntOffset(p.width / 2, point.offset.y.toInt() + paddingTop)
        HighlightPosition.POINT -> -IntOffset(p.width / 2, p.height)
        else -> IntOffset(0, 0) //TODO Implement placement of other positions
    }

    val popupLayoutModifier: MeasureScope.(Measurable, Constraints) -> MeasureResult = { m, c ->
        val placeable = m.measure(c)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(getPlacementOffset(placeable))
        }
    }

    Box(Modifier.layout(popupLayoutModifier)) {
        content(point)
    }
}

@Composable
fun HighlightPopupScope.RoundedCardHighlightPopup(
    modifier: Modifier = Modifier,
    cardContent: @Composable BoxScope.(Point) -> Unit
) {
    val popupShape = HighlightPopupShape(position)
    val popupPositioner: Density.() -> IntOffset = {
        IntOffset(
            point.xPos.toInt() + paddingStart,
            point.yPos.toInt() + paddingTop
        ) + if (position == HighlightPosition.POINT) {
            IntOffset(0, -popupShape.arrowSize.toPx().toInt())
        } else {
            IntOffset(0, 0)
        }
    }

    Card(
        Modifier
            .offset(popupPositioner)
            .padding(if (position != HighlightPosition.POINT) 4.dp else 0.dp)
            .sizeIn(
                minWidth = popupShape.suggestedMinWidth,
                minHeight = popupShape.suggestedMinHeight
            ).then(modifier),
        backgroundColor = Color.White,
        shape = popupShape,
        elevation = 4.dp
    ) {
        Box(content = { cardContent(point) })
    }
}
