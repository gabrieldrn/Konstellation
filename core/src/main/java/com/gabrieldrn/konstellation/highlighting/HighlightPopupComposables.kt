package com.gabrieldrn.konstellation.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    fun getPlacementOffset(p: Placeable) = when (scope.contentPosition) {
        HighlightContentPosition.Top ->
            -IntOffset(p.width / 2, scope.point.yPos.toInt() + scope.paddingTop)
        HighlightContentPosition.Bottom ->
            -IntOffset(p.width / 2, 0)
        HighlightContentPosition.Start,
        HighlightContentPosition.End ->
            -IntOffset(0, p.height / 2) + IntOffset(0, scope.paddingTop)
        HighlightContentPosition.Point ->
            -IntOffset(p.width / 2, p.height)
    }

    fun getAlignment() = when (scope.contentPosition) {
        HighlightContentPosition.Bottom -> Alignment.BottomStart
        HighlightContentPosition.End -> Alignment.TopEnd
        else -> Alignment.TopStart
    }

    Box(
        Modifier
            .offset { scope.getContentOffset() }
            .align(getAlignment())
            .layout { m, c ->
                val placeable = m.measure(c)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(getPlacementOffset(placeable))
                }
            }
    ) {
        content(scope)
    }
}

// TODO Move all the highlighting code to another module and duplicate it to provide M2 + M3
//   versions.
/**
 * Creates a popup to highlight content from the chart with a [shape]. The default shape, a
 * [HighlightPopupShape], is a rounded card with an arrow placed in accordance with the positions
 * of the highlight defined in the chart composable parameters, so as it will point towards the
 * highlighted value.
 * The popup is using the material 3 [Surface], so the arguments of this popup are the same as the
 * ones from [Surface].
 * Contents of the highlighting popup are defined in [content].
 */
@Composable
fun HighlightScope.HighlightPopup(
    modifier: Modifier = Modifier,
    shape: HighlightPopupShape = HighlightPopupShape(contentPosition),
    color: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 5.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .padding(shape.arrowSize)
            .sizeIn(
                minWidth = shape.suggestedMinWidth,
                minHeight = shape.suggestedMinHeight
            )
            .then(modifier),
        color = color,
        shape = shape,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    ) {
        Box(content = content)
    }
}

//TODO Preview highlight popup for every position.
