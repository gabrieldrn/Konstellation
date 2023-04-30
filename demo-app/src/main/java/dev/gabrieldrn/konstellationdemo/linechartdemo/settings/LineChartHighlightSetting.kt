package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.South
import androidx.compose.material.icons.filled.West
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton

/**
 * Composable that allows the user to change the highlight popup positions of the LineChart.
 */
@Composable
fun LineChartHighlightSetting(
    contentPositions: Set<HighlightContentPosition>,
    onUpdateHighlightConfig: (Set<HighlightContentPosition>) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Highlight popup positions", modifier = modifier) {
        @Composable
        fun PositionToggleButton(position: HighlightContentPosition, imageVector: ImageVector) {
            ToggleIconButton(
                toggled = contentPositions.contains(position),
                onToggleChange = { toggled ->
                    onUpdateHighlightConfig(
                         if (toggled) {
                            contentPositions + position
                        } else {
                            contentPositions - position
                        }
                    )
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
