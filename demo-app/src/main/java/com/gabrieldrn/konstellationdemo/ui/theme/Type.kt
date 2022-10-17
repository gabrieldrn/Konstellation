@file:Suppress("MagicNumber")

package com.gabrieldrn.konstellationdemo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.sp
import com.gabrieldrn.konstellationdemo.R

/**
 * [Figtree](https://fonts.google.com/specimen/Figtree) font.
 * Geometric, with beautiful numbers.
 */
private val FigtreeFamily = FontFamily(
    Font(R.font.figtree_light, weight = FontWeight.Light),
    Font(R.font.figtree_regular),
    Font(R.font.figtree_medium, weight = FontWeight.Medium),
    Font(R.font.figtree_semibold, weight = FontWeight.SemiBold),
    Font(R.font.figtree_bold, weight = FontWeight.Bold),
    Font(R.font.figtree_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.figtree_black, weight = FontWeight.Black),
)

/**
 * Typography definition for Konstellation.
 */
val Typography = Typography(defaultFontFamily = FigtreeFamily)
