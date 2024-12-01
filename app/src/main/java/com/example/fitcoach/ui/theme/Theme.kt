package com.example.fitcoach.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    secondary = Blue,
    tertiary = Green,
    background = BackgroundLight,
    surface = Color.White,      
    surfaceVariant = Color.White.copy(alpha = 0.7f),
    primaryContainer = Orange.copy(alpha = 0.1f),
    secondaryContainer = Blue.copy(alpha = 0.1f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextPrimaryLight.copy(alpha = 0.7f),
    onPrimaryContainer = Orange,
    onSecondaryContainer = Blue
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    secondary = Blue,
    tertiary = Green,
    background = BackgroundDark,
    surface = DarkBlueDark,
    surfaceVariant = DarkBlueLight,
    primaryContainer = Orange.copy(alpha = 0.1f),
    secondaryContainer = Blue.copy(alpha = 0.1f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextPrimary.copy(alpha = 0.7f),
    onPrimaryContainer = Orange,
    onSecondaryContainer = Blue
)

@Composable
fun FitCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}