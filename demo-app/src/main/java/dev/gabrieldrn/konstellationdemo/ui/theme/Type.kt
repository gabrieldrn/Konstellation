@file:Suppress("MagicNumber")

package dev.gabrieldrn.konstellationdemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.gabrieldrn.konstellationdemo.R

/**
 * [Krona One](https://fonts.google.com/specimen/Krona+One) typeface for display and headlines.
 */
private val KronaOneFamily = FontFamily(
    Font(R.font.krona_one_regular)
)

/**
 * [Figtree](https://fonts.google.com/specimen/Figtree) typeface for contents.
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
val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = KronaOneFamily),
        displayMedium = displayMedium.copy(fontFamily = KronaOneFamily),
        displaySmall = displaySmall.copy(fontFamily = KronaOneFamily),
        headlineLarge = headlineLarge.copy(fontFamily = KronaOneFamily),
        headlineMedium = headlineMedium.copy(fontFamily = KronaOneFamily),
        headlineSmall = headlineSmall.copy(fontFamily = KronaOneFamily),
        titleLarge = titleLarge.copy(fontFamily = FigtreeFamily),
        titleMedium = titleMedium.copy(fontFamily = FigtreeFamily),
        titleSmall = titleSmall.copy(fontFamily = FigtreeFamily),
        bodyLarge = bodyLarge.copy(fontFamily = FigtreeFamily),
        bodyMedium = bodyMedium.copy(fontFamily = FigtreeFamily),
        bodySmall = bodySmall.copy(fontFamily = FigtreeFamily),
        labelLarge = labelLarge.copy(fontFamily = FigtreeFamily),
        labelMedium = labelMedium.copy(fontFamily = FigtreeFamily),
        labelSmall = labelSmall.copy(fontFamily = FigtreeFamily)
    )
}
