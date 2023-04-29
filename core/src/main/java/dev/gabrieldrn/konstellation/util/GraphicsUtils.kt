package dev.gabrieldrn.konstellation.util

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

internal val Color.redInt: Int get() = (255 * red).roundToInt()
internal val Color.greenInt: Int get() = (255 * green).roundToInt()
internal val Color.blueInt: Int get() = (255 * blue).roundToInt()
internal fun Color.toInt() = android.graphics.Color.argb(255, redInt, greenInt, blueInt)
