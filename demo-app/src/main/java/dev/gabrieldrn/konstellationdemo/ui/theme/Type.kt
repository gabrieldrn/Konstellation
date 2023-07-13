package dev.gabrieldrn.konstellationdemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.gabrieldrn.konstellationdemo.R

private val SoraFamily = FontFamily(
    Font(R.font.sora_thin, weight = FontWeight.Thin),
    Font(R.font.sora_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic),
    Font(R.font.sora_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.sora_extralightitalic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
    Font(R.font.sora_light, weight = FontWeight.Light),
    Font(R.font.sora_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.sora_regular),
    Font(R.font.sora_italic, style = FontStyle.Italic),
    Font(R.font.sora_medium, weight = FontWeight.Medium),
    Font(R.font.sora_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.sora_semibold, weight = FontWeight.SemiBold),
    Font(R.font.sora_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(R.font.sora_bold, weight = FontWeight.Bold),
    Font(R.font.sora_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.sora_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.sora_extrabolditalic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
)

private val TabularFamily = FontFamily(
    Font(R.font.tabular_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.tabular_regular),
    Font(R.font.tabular_italic, style = FontStyle.Italic),
    Font(R.font.tabular_medium, weight = FontWeight.Medium),
    Font(R.font.tabular_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.tabular_semibold, weight = FontWeight.SemiBold),
    Font(R.font.tabular_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(R.font.tabular_bold, weight = FontWeight.Bold),
    Font(R.font.tabular_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
)

/**
 * Typography definition for Konstellation.
 */
val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = SoraFamily),
        displayMedium = displayLarge.copy(fontFamily = SoraFamily),
        displaySmall = displayLarge.copy(fontFamily = SoraFamily),

        headlineLarge = headlineLarge.copy(fontFamily = TabularFamily),
        headlineMedium = headlineMedium.copy(fontFamily = TabularFamily),
        headlineSmall = headlineSmall.copy(fontFamily = TabularFamily),

        titleLarge = titleLarge.copy(fontFamily = TabularFamily),
        titleMedium = titleMedium.copy(fontFamily = TabularFamily),
        titleSmall = titleSmall.copy(fontFamily = TabularFamily),

        bodyLarge = bodyLarge.copy(fontFamily = TabularFamily),
        bodyMedium = bodyMedium.copy(fontFamily = TabularFamily),
        bodySmall = bodySmall.copy(fontFamily = TabularFamily),

        labelLarge = labelLarge.copy(fontFamily = TabularFamily),
        labelMedium = labelMedium.copy(fontFamily = TabularFamily),
        labelSmall = labelSmall.copy(fontFamily = TabularFamily)
    )
}
