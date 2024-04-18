package dev.gabrieldrn.konstellationdemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.gabrieldrn.konstellationdemo.R

/**
 * Iosevka SS08 Extended font family.
 */
private val IosevkaFamily = FontFamily(
    Font(R.font.iosevka_ss08_ex_thin, weight = FontWeight.Thin),
    Font(R.font.iosevka_ss08_ex_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic),
    Font(R.font.iosevka_ss08_ex_extralight, weight = FontWeight.ExtraLight),
    Font(
        R.font.iosevka_ss08_ex_extralightitalic,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic
    ),
    Font(R.font.iosevka_ss08_ex_light, weight = FontWeight.Light),
    Font(R.font.iosevka_ss08_ex_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.iosevka_ss08_ex),
    Font(R.font.iosevka_ss08_ex_italic, style = FontStyle.Italic),
    Font(R.font.iosevka_ss08_ex_medium, weight = FontWeight.Medium),
    Font(R.font.iosevka_ss08_ex_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.iosevka_ss08_ex_semibold, weight = FontWeight.SemiBold),
    Font(
        R.font.iosevka_ss08_ex_semibolditalic,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic
    ),
    Font(R.font.iosevka_ss08_ex_bold, weight = FontWeight.Bold),
    Font(R.font.iosevka_ss08_ex_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.iosevka_ss08_ex_extrabold, weight = FontWeight.ExtraBold),
    Font(
        R.font.iosevka_ss08_ex_extrabolditalic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic
    ),
    Font(R.font.iosevka_ss08_ex_heavy, weight = FontWeight.Black),
    Font(R.font.iosevka_ss08_ex_heavyitalic, weight = FontWeight.Black, style = FontStyle.Italic),
)

/**
 * Typography definition for Konstellation.
 */
val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.Light
        ),
        displayMedium = displayLarge.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.Light
        ),
        displaySmall = displayLarge.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.Light
        ),

        headlineLarge = headlineLarge.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.SemiBold
        ),
        headlineMedium = headlineMedium.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.SemiBold
        ),
        headlineSmall = headlineSmall.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.SemiBold
        ),

        titleLarge = titleLarge.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.SemiBold
        ),
        titleMedium = titleMedium.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.SemiBold
        ),
        titleSmall = titleSmall.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.SemiBold
        ),

        bodyLarge = bodyLarge.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.Medium
        ),
        bodyMedium = bodyMedium.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.Medium
        ),
        bodySmall = bodySmall.copy(
            fontFamily = IosevkaFamily,
            fontWeight = FontWeight.Medium
        ),

        labelLarge = labelLarge.copy(fontFamily = IosevkaFamily),
        labelMedium = labelMedium.copy(fontFamily = IosevkaFamily),
        labelSmall = labelSmall.copy(fontFamily = IosevkaFamily)
    )
}
