package com.gabrieldrn.konstellation.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*

/**
 * Creates and places a [Box] ready to compose a highlight popup within a highlighting [scope] with
 * a given [content] provided by the user.
 */
@Composable
fun BoxScope.BoxedPopup(
    scope: HighlightScope,
    content: @Composable HighlightScope.() -> Unit
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
        content(scope)
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
fun HighlightScope.HighlightPopup(
    modifier: Modifier = Modifier,
    shape: HighlightPopupShape = HighlightPopupShape(position),
    backgroundColor: Color = if (MaterialTheme.colors.isLight) Color.White else Color.Black,
    content: @Composable BoxScope.() -> Unit
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
        Box(content = content)
    }
}

//TODO Preview highlight popup for every position.
