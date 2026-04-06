package ru.hey_savvy.sigm_app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SigmPurple = Color(0xFF7F77DD)
val SigmPurpleLight = Color(0xFFAFA9EC)
val SigmPurpleDark = Color(0xFF534AB7)

private val LightColorScheme = lightColorScheme(
    primary = SigmPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEEEDFE),
    onPrimaryContainer = SigmPurpleDark,
    secondary = SigmPurpleLight,
    onSecondary = Color.White,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE8E8E8),
    onSurfaceVariant = Color(0xFF49454F)
)

private val DarkColorScheme = darkColorScheme(
    primary = SigmPurpleLight,
    onPrimary = Color(0xFF26215C),
    primaryContainer = SigmPurpleDark,
    onPrimaryContainer = Color(0xFFEEEDFE),
    secondary = SigmPurple,
    onSecondary = Color.White,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

@Composable
fun SigmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}