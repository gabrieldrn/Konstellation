package com.gabrieldrn.konstellation.style

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color

data class TextDrawStyle(
    override var color: Color = Color.Black,
    var typeface: Typeface = Typeface.DEFAULT,
) : DrawStyle //Useful ?
