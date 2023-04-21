package com.gabrieldrn.konstellation.highlighting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.plotting.Point

/**
 * Creates and places a [Box] ready to compose a highlight popup within a highlighting [scope] with
 * a given [content] provided by the user.
 */
@Composable
public fun BoxScope.HighlightBox(
    scope: HighlightScope,
    chartTopPaddingPx: Int,
    chartStartPaddingPx: Int,
    modifier: Modifier = Modifier,
    content: @Composable HighlightScope.() -> Unit
) {
    Box(
        modifier = modifier
            // Moves the popup near the highlighted point.
            .popupContentOffset(
                scope.point,
                scope.contentPosition,
                chartTopPaddingPx,
                chartStartPaddingPx
            )
            // Helps to place the popup at the bottom or at the end of the chart.
            .align(
                when (scope.contentPosition) {
                    HighlightContentPosition.Bottom -> Alignment.BottomStart
                    HighlightContentPosition.End -> Alignment.TopEnd
                    else -> Alignment.TopStart
                }
            )
            // Centers the popup to align it with the highlighted point.
            .alignWithPoint(
                scope.point,
                scope.contentPosition,
                chartTopPaddingPx
            )
    ) {
        content(scope)
    }
}

/**
 * Offset the popup content to place it near the highlighted point.
 */
private fun Modifier.popupContentOffset(
    point: Point,
    contentPosition: HighlightContentPosition,
    chartTopPaddingPx: Int,
    chartStartPaddingPx: Int,
) = this.offset {
    when (contentPosition) {
        HighlightContentPosition.Top,
        HighlightContentPosition.Point ->
            IntOffset(
                point.xPos.toInt() + chartStartPaddingPx,
                point.yPos.toInt() + chartTopPaddingPx
            )

        HighlightContentPosition.Bottom ->
            IntOffset(point.xPos.toInt() + chartStartPaddingPx, 0)

        HighlightContentPosition.Start ->
            IntOffset(0, point.yPos.toInt())

        HighlightContentPosition.End ->
            IntOffset(0, point.yPos.toInt())
    }
}

/**
 * Aligns the popup box content with the highlighted point.
 */
private fun Modifier.alignWithPoint(
    point: Point,
    contentPosition: HighlightContentPosition,
    chartTopPaddingPx: Int,
) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height) {
        placeable.place(
            when (contentPosition) {
                HighlightContentPosition.Top ->
                    -IntOffset(placeable.width / 2, point.yPos.toInt() + chartTopPaddingPx)

                HighlightContentPosition.Bottom ->
                    -IntOffset(placeable.width / 2, 0)

                HighlightContentPosition.Start,
                HighlightContentPosition.End ->
                    -IntOffset(0, placeable.height / 2) + IntOffset(0, chartTopPaddingPx)

                HighlightContentPosition.Point ->
                    -IntOffset(placeable.width / 2, placeable.height)
            }
        )
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
public fun HighlightScope.HighlightPopup(
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
            ),
        color = color,
        shape = shape,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    ) {
        Box(content = content)
    }
}

private class HighlightPositionParameterProvider :
    PreviewParameterProvider<HighlightContentPosition> {
    override val values = HighlightContentPosition.values().asSequence()
}

@Preview(showBackground = true)
@Composable
private fun HighlightPopupPreview(
    @PreviewParameter(HighlightPositionParameterProvider::class) pos: HighlightContentPosition
) {
    val point = Point(1f, 2f)
    Box(Modifier.padding(8.dp)) {
        HighlightScope(point, pos).run {
            HighlightPopup {
                Text(
                    text = "📍 ${point.x};${point.y}",
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
