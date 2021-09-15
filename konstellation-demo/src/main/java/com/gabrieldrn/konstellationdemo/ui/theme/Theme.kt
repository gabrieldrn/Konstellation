package com.gabrieldrn.konstellationdemo.ui.theme

import android.R.color.*
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.*

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun KonstellationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Using Material You
        if (darkTheme) darkColors(
            primary = colorResource(system_accent1_300),
            primaryVariant = colorResource(system_accent1_100),
            secondary = colorResource(system_accent2_200),
            secondaryVariant = colorResource(system_accent2_400),
            surface = colorResource(system_neutral1_900),
        ) else lightColors(
            primary = colorResource(system_accent1_500),
            primaryVariant = colorResource(system_accent1_700),
            secondary = colorResource(system_accent2_200),
            secondaryVariant = colorResource(system_accent2_400),
            surface = colorResource(system_accent1_10)
        )
    } else if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
