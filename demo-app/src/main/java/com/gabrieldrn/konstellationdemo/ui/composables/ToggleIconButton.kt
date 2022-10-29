package com.gabrieldrn.konstellationdemo.ui.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

/**
 * Fixed size for a [ToggleIconButton].
 */
val toggleIconButtonSize = 48.dp

@Composable
fun ToggleIconButton(
    toggled: Boolean,
    onToggleChange: (Boolean) -> Unit,
    imageVector: ImageVector,
    toggledColor: Color = MaterialTheme.colorScheme.primary,
    disabledColor: Color = LocalContentColor.current.copy(alpha = 0.12f),
    toggledImageVectorTint: Color = MaterialTheme.colorScheme.onPrimary,
    disabledImageVectorTint: Color = LocalContentColor.current,
    modifier: Modifier = Modifier
) {
    val background by animateColorAsState(
        targetValue = if (toggled) toggledColor else disabledColor,
        animationSpec = tween()
    )
    val imageVectorTint by animateColorAsState(
        targetValue = if (toggled) toggledImageVectorTint else disabledImageVectorTint,
        animationSpec = tween()
    )
    Box(
        modifier = modifier
            .size(toggleIconButtonSize)
            .background(
                color = background,
                shape = CircleShape
            )
            .toggleable(
                onValueChange = onToggleChange,
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
