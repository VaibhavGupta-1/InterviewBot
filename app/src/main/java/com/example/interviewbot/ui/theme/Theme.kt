// In ui/theme/Theme.kt
package com.example.interviewbot.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = LightGray,
    primaryContainer = NavyBlue,
    onPrimaryContainer = LightGray,
    secondary = VibrantRed,
    onSecondary = Color.White,
    background = DarkBlue,
    onBackground = OffWhite,
    surface = ElectricBlue,
    onSurface = OffWhite,
    surfaceVariant = NavyBlue,
    onSurfaceVariant = LightGray,
    error = VibrantRed
)

@Composable
fun InterviewBotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // We will force dark theme for a consistent, professional look
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}