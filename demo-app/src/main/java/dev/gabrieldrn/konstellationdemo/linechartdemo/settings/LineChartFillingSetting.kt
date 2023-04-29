package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton

/**
 * Composable that allows the user to change the filling of the LineChart.
 */
@Composable
fun LineChartFillingSetting(
    brush: Brush?,
    onUpdateBrush: (Brush?) -> Unit,
    modifier: Modifier = Modifier
) {
    val solidColor = SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = .75f))
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
    )
    SettingSurface(title = "Filling", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            ToggleIconButton(
                toggled = brush is SolidColor,
                onToggleChange = { onUpdateBrush(solidColor) },
                imageVector = Icons.Default.FormatColorFill,
            )
            ToggleIconButton(
                toggled = brush is ShaderBrush,
                onToggleChange = { onUpdateBrush(gradientBrush) },
                imageVector = Icons.Default.Gradient,
            )
            ToggleIconButton(
                toggled = brush == null,
                onToggleChange = { onUpdateBrush(null) },
                imageVector = Icons.Default.FormatColorReset,
            )
        }
    }
}
