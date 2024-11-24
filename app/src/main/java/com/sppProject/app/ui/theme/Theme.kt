package com.sppProject.app.ui.theme

import android.R.color.white
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenDarkVariant,
    onPrimary = OffWhite,
    secondary = LightGreenDark,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = OffWhite,
    onSurface = LightGrayText
)

private val LightColorScheme = lightColorScheme(
    primary = White,
    onPrimary = DarkerGreen,
    secondary = DarkerGreen,
    tertiary = LightGreenDark,
    background = White,
    onBackground = LightGrayText,
    surface = OffWhite,
    onSurface = BlackAccent
)


@Composable
fun SPPProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,  // Set this to false to avoid using dynamic colors for now
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
