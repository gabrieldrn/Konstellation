package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton

@Composable
internal fun LineChartHighlightSetting(
    highlightPositions: Set<HighlightContentPosition>,
    onAddHighlightPosition: (HighlightContentPosition) -> Unit,
    onRemoveHighlightPosition: (HighlightContentPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Highlight popup positions", modifier = modifier) {
        @Composable
        fun PositionToggleButton(position: HighlightContentPosition, imageVector: ImageVector) {
            ToggleIconButton(
                toggled = highlightPositions.contains(position),
                onToggleChange = { toggled ->
                    if (toggled) onAddHighlightPosition(position)
                    else onRemoveHighlightPosition(position)
                },
                imageVector = imageVector
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            PositionToggleButton(HighlightContentPosition.Top, Icons.Default.North)
            PositionToggleButton(HighlightContentPosition.Bottom, Icons.Default.South)
            PositionToggleButton(HighlightContentPosition.Start, Icons.Default.West)
            PositionToggleButton(HighlightContentPosition.End, Icons.Default.East)
            Box(
                Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )
            PositionToggleButton(HighlightContentPosition.Point, Icons.Default.PushPin)
        }
    }
}
