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

class HighlightPopupScope(
    val point: Point,
    val position: HighlightPosition,
    val chartPaddings: PaddingValues
) {
    internal var paddingTop = 0
    internal var paddingStart = 0

    @Composable
    internal fun ComputePaddings() {
        LocalDensity.current.run {
            paddingTop = chartPaddings.calculateTopPadding().toPx().toInt()
            paddingStart = chartPaddings.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
        }
    }
}

@Composable
internal fun BoxScope.BoxedHighlightPopup(
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

    Box(Modifier.align(getAlignment()).layout(popupLayoutModifier)) {
        content(scope, scope.point)
    }
}

@Composable
fun HighlightPopupScope.HighlightPopup(
    modifier: Modifier = Modifier,
    popupShape: HighlightPopupShape = HighlightPopupShape(position),
    backgroundColor: Color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
    cardContent: @Composable BoxScope.(Point) -> Unit
) {
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

    Card(
        Modifier
            .offset(popupPositioner)
            .padding(popupShape.arrowSize)
            .sizeIn(
                minWidth = popupShape.suggestedMinWidth,
                minHeight = popupShape.suggestedMinHeight
            )
            .then(modifier),
        backgroundColor = backgroundColor,
        shape = popupShape,
        elevation = 4.dp
    ) {
        Box(content = { cardContent(point) })
    }
}
