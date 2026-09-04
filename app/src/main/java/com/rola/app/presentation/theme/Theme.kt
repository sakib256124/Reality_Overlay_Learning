package com.rola.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = RolaGreen,
    secondary = RolaBlue,
    tertiary = RolaCopper,
    background = RolaBackgroundLight,
    surface = RolaSurfaceLight,
)

private val DarkColors = darkColorScheme(
    primary = RolaGreen,
    secondary = RolaBlue,
    tertiary = RolaCopper,
    background = RolaBackgroundDark,
    surface = RolaSurfaceDark,
)

@Composable
fun ROLATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = RolaTypography,
        content = content,
    )
}
