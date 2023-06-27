package dev.gabrieldrn.konstellationdemo.ui.composables

import android.content.Context
import android.media.AudioManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlinx.coroutines.launch

/**
 * Fixed size for a [ToggleIconButton].
 */
val toggleIconButtonSize = 48.dp

/**
 * A toggleable icon button that can be used to toggle a setting.
 */
@Composable
fun ToggleIconButton(
    toggled: Boolean,
    onToggleChange: (Boolean) -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    toggledColor: Color = MaterialTheme.colorScheme.primary,
    disabledColor: Color = LocalContentColor.current.copy(alpha = 0.12f),
    toggledImageVectorTint: Color = MaterialTheme.colorScheme.onPrimary,
    disabledImageVectorTint: Color = LocalContentColor.current,
) {
    val background by animateColorAsState(
        targetValue = if (toggled) toggledColor else disabledColor,
        animationSpec = tween(), label = "ToggleIconButtonBackground"
    )
    val imageVectorTint by animateColorAsState(
        targetValue = if (toggled) toggledImageVectorTint else disabledImageVectorTint,
        animationSpec = tween(), label = "ToggleIconButtonImageVectorTint"
    )
    val audioManager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val buttonScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .size(toggleIconButtonSize)
            .background(
                color = background,
                shape = CircleShape
            )
            .toggleable(
                onValueChange = {
                    buttonScope.launch {
                        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
                    }
                    onToggleChange(it)
                },
                value = toggled,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = imageVectorTint
        )
    }
}

@Preview
@Composable
private fun ToggleIconButtonPreview() {
    KonstellationTheme {
        var toggled by remember { mutableStateOf(false) }
        ToggleIconButton(
            toggled = toggled,
            onToggleChange = { toggled = it },
            imageVector = Icons.Default.Check,
        )
    }
}
