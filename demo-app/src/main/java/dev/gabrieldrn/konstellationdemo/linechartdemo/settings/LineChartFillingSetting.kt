package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

import android.content.res.Resources
import android.graphics.BitmapShader
import android.graphics.Shader
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import dev.gabrieldrn.konstellationdemo.R
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
    val resources = LocalContext.current.resources
    val primaryColor = MaterialTheme.colorScheme.primary
    val solidColor = SolidColor(primaryColor.copy(alpha = .75f))
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(primaryColor, Color.Transparent)
    )
    val dotsBrush = DotsBrush(resources, primaryColor)

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
                toggled = brush == gradientBrush,
                onToggleChange = { onUpdateBrush(gradientBrush) },
                imageVector = Icons.Default.Gradient,
            )
            ToggleIconButton(
                toggled = brush is DotsBrush,
                onToggleChange = { onUpdateBrush(dotsBrush) },
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

/**
 * Brush that uses a bitmap shader to draw dots.
 * @property resources The resources to use to get the bitmap.
 * @property primaryColor The color to tint the bitmap with.
 */
class DotsBrush(
    private val resources: Resources,
    private val primaryColor: Color
) : ShaderBrush() {

    override fun createShader(size: Size): Shader {
        return BitmapShader(
            ResourcesCompat
                .getDrawable(resources, R.drawable.ic_dots, null)!!
                .apply { setTint(primaryColor.toArgb()) }
                .toBitmap(),
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )
    }
}
