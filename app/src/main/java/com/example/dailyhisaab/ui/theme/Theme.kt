package com.example.dailyhisaab.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = EmeraldDark,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = ForestGreen,
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = ChampagneGold,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = SecondarySurface,
    onSecondaryContainer = TextPrimary,
    tertiary = SecondaryGreen,
    onTertiary = Color(0xFFFFFFFF),
    background = WarmIvory,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    error = ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    outline = OutlineColor,
    surfaceVariant = SecondarySurface,
    onSurfaceVariant = TextSecondary
)

private val DarkColorScheme = darkColorScheme(
    primary = SecondaryGreen,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = EmeraldDark,
    onPrimaryContainer = Color(0xFFE2E8F0),
    secondary = ChampagneGold,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF2C3632),
    onSecondaryContainer = Color(0xFFE2E8F0),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE2E8F0),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF404945),
    surfaceVariant = Color(0xFF2C3632),
    onSurfaceVariant = Color(0xFFBFC9C4)
)

@Composable
fun DailyHisaabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic color to enforce brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
