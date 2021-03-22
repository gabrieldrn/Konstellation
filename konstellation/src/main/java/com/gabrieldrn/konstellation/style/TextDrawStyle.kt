package com.gabrieldrn.konstellation.style

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color

data class TextDrawStyle(
    var typeface: Typeface = Typeface.DEFAULT,
    var color: Color = Color.Black
) : DrawStyle //Useful ?
