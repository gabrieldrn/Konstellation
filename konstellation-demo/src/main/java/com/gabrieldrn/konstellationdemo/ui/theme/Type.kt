@file:Suppress("MagicNumber")

package com.gabrieldrn.konstellationdemo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.sp
import com.gabrieldrn.konstellationdemo.R

private val manropeFamily = FontFamily(
    Font(R.font.manrope_light, weight = FontWeight.Light),
    Font(R.font.manrope_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.manrope_regular),
    Font(R.font.manrope_medium, weight = FontWeight.Medium),
    Font(R.font.manrope_semibold, weight = FontWeight.SemiBold),
    Font(R.font.manrope_bold, weight = FontWeight.Bold),
    Font(R.font.manrope_extrabold, weight = FontWeight.ExtraBold)
)

//Manrope glyphs are not quite aligned with the baseline.
private val manropeBaselineShift = BaselineShift(0.2f)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = manropeFamily,
    body1 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        baselineShift = manropeBaselineShift
    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        baselineShift = manropeBaselineShift
    ),
    /*
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )*/
)
