package dev.gabrieldrn.konstellationdemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.gabrieldrn.konstellationdemo.R

 private val InterFamily = FontFamily(
    Font(R.font.inter_thin, weight = FontWeight.Thin),
    Font(R.font.inter_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.inter_light, weight = FontWeight.Light),
    Font(R.font.inter_regular),
    Font(R.font.inter_medium, weight = FontWeight.Medium),
    Font(R.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
    Font(R.font.inter_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.inter_black, weight = FontWeight.Black),
)

/**
 * Typography definition for Konstellation.
 */
val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.Light
        ),
        displayMedium = displayLarge.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.Light
        ),
        displaySmall = displayLarge.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.Light
        ),

        headlineLarge = headlineLarge.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold
        ),
        headlineMedium = headlineMedium.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold
        ),
        headlineSmall = headlineSmall.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold
        ),

        titleLarge = titleLarge.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold
        ),
        titleMedium = titleMedium.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold
        ),
        titleSmall = titleSmall.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.SemiBold
        ),

        bodyLarge = bodyLarge.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium
        ),
        bodyMedium = bodyMedium.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium
        ),
        bodySmall = bodySmall.copy(
            fontFamily = InterFamily,
            fontWeight = FontWeight.Medium
        ),

        labelLarge = labelLarge.copy(fontFamily = InterFamily),
        labelMedium = labelMedium.copy(fontFamily = InterFamily),
        labelSmall = labelSmall.copy(fontFamily = InterFamily)
    )
}
